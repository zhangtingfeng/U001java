import ajax from "@f/components/ajax";
import script from "@f/components/script";
const TIMEOUT = 30000
let CHUNKS = {}

function createRequire(chunk) {
  function __require_from_chunk(chunk, id) {
    let obj;
    if (chunk.modules[id]) {
      obj = chunk.modules[id]
    } else {
      let factory = chunk.data[1][id]
      if (factory) {
        let m = {
          i: id,
          l: false,
          hot: {
            accept() {
              console.log(arguments)
            }, dispose() {
              console.log(arguments)
            }
          },
          exports: {}
        }

        try {
          factory.call(m.exports, m, m.exports, __require)
        } catch (err) {
          console.error(err)
          debugger
          throw err
        }
        m.l = true
        chunk.modules[id] = m
        obj = m
      } else {
        for (let c of chunk.children) {
          obj = __require_from_chunk(c, id)
          if (obj) {
            break;
          }
        }
      }
    }
    return obj;
  }


  function __require(id) {
    // get root chunk
    let rootChunk = chunk
    while (rootChunk.parent) {
      rootChunk = rootChunk.parent
    }

    let m = __require_from_chunk(rootChunk, id)
    if (!m) {
      throw `load chunk [${id}] failed!`
    }
    return m.exports
  }

  __require.p = ""
  __require.base = chunk.base
  __require.version = chunk.version

  // expose the modules object (__webpack_modules__)
  __require.m = chunk.data[1]


  // expose the module cache
  __require.c = chunk.modules

  // define getter function for harmony exports
  __require.d = function (exports, name, getter) {
    if (!__require.o(exports, name)) {
      Object.defineProperty(exports, name, { enumerable: true, get: getter })
    }
  }

  // define __esModule on exports
  __require.r = function (exports) {
    if (typeof Symbol !== 'undefined' && Symbol.toStringTag) {
      Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' })
    }
    Object.defineProperty(exports, '__esModule', { value: true })
  }

  // create a fake namespace object
  // mode & 1: value is a module id, require it
  // mode & 2: merge all properties of value into the ns
  // mode & 4: return value when already ns object
  // mode & 8|1: behave like require
  __require.t = function (value, mode) {
    if (mode & 1) value = __require(value)
    if (mode & 8) return value
    if (mode & 4 && typeof value === 'object' && value && value.__esModule) return value
    var ns = Object.create(null)
    __require.r(ns)
    Object.defineProperty(ns, 'default', { enumerable: true, value: value })
    if (mode & 2 && typeof value != 'string')
      for (var key in value)
        __require.d(
          ns,
          key,
          function (key) {
            return value[key]
          }.bind(null, key)
        )
    return ns
  }

  // getDefaultExport function for compatibility with non-harmony modules
  __require.n = function (module) {
    var getter =
      module && module.__esModule
        ? function getDefault() {
          return module['default']
        }
        : function getModuleExports() {
          return module
        }
    __require.d(getter, 'a', getter)
    return getter
  }

  // Object.prototype.hasOwnProperty.call
  __require.o = function (object, property) {
    return Object.prototype.hasOwnProperty.call(object, property)
  }

  __require.e = async (chunkId) => {
    let path = chunk.entries[chunkId]
    let childChunk = await load(__require.base, path, __require.version, undefined, chunk)
    chunk.children.push(childChunk)
    return childChunk;
  };

  return __require
}

const LoadingChunk = []
{
  let jsonpArray = window.webpackJsonp;
  let oldJsonpFunction = jsonpArray.push.bind(jsonpArray);
  jsonpArray.push = data => {
    if (LoadingChunk.length > 0) {
      let lc = LoadingChunk.pop()
      let chunk = { data, modules: {}, children: [] }
      for (let f of ["url", "base", "version", "parent", "entries"]) {
        chunk[f] = lc[f]
      }
      CHUNKS[chunk.url] = chunk

      try {
        chunk.r = createRequire(chunk)
      } catch (err) {
        lc.reject(`${chunk.url} failed.`)
      }
      clearTimeout(lc.failedHandler)
      lc.resolve(chunk)
    } else {
      oldJsonpFunction(data)
    }
  }
}

function loadChunk(base, path, version, parentChunk, entries) {
  return new Promise((resolve, reject) => {
    let url = `${base}${path}`
    let idx = -1;
    let failedHandler = setTimeout(() => {
      LoadingChunk.splice(idx, 1)
      console.log(`timeout with ${url}`)
      // reject(`timeout with ${url}`)
    }, TIMEOUT);
    idx = LoadingChunk.push({ url, base, path, version, parent: parentChunk, resolve, reject, failedHandler, entries })
    script(url);
  })
}

async function load(base, path, version, parentChunk, entries) {
  let url = `${base}${path}`
  if (!CHUNKS[url]) {
    await loadChunk(base, path, version, parentChunk, entries)
  }

  let chunk = CHUNKS[url];
  if (chunk.data.length === 3) {
    let id = chunk.data[2]
    while (Array.isArray(id)) {
      id = id[0]
    }
    await chunk.r(id);
  }

  return chunk;
}

async function loadModule(moduleData) {
  if (!/\/$/.test(moduleData.url)) {
    moduleData.url += "/";
  }

  // load entries.json
  if (!moduleData.entries) {
    try {
      moduleData.entries = await ajax.get(`${moduleData.url}entries.json?${moduleData.version}`)
    } catch (err) {
      throw `load [${moduleData.url}entries.json] failed.`
    }
  }

  // load index.js
  return await load(moduleData.url, moduleData.entries.index, moduleData.version, null, moduleData.entries)
}
export default loadModule

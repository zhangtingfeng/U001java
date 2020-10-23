import { ajax } from "@f/vendor";

function parseLocaleMsg(msg) {
	let tmp = msg.split('|')
	return [tmp[0], tmp[1] ? tmp[1] : tmp[0]]
}

function getPathName(name) {
	let t = name.lastIndexOf("/")
	let n = name.substr(t + 1)
	let p = name.substr(0, t + 1)
	return [p, n]
}

const METAS = {}
const METAMSGS={}

export function load(name, params) {
	let meta = METAS[name]

	let $d = $.Deferred()
	let promise = METAS[name] = $d.promise()
	let pathName = getPathName(name)
	var reqUrl = `api/${pathName[0]}listing-sale/getMetaInfo`
	for (let k in params) {
		if (reqUrl.indexOf("?") != -1) {
			reqUrl += '&' + k + "=" + params[k];
		} else {
			reqUrl += '?' + k + "=" + params[k];
		}
	}

	ajax.get(reqUrl).then(data => {
		if (data.meta) {
			data.meta.pathName = pathName
			data.meta.name2 = pathName.join("")
			data.meta.url = reqUrl
			METAS[data.meta.name2] = data.meta
			if (data.meta.children) {
				for (let c in data.meta.children) {
					let m = data.meta.children[c]
					m.pathName = [pathName[0], m.name]
					m.name2 = m.pathName.join("")
					m.url = reqUrl
					METAS[m.name2] = m
				}
			}
			$d.resolve(METAS[name])
		} else
			$d.reject(2)
	}).catch((err) => {
		console.error(err)
		$d.reject(1)
	})
	return promise
}

export function msgTrans(name) {
	if (METAMSGS[name]) return METAMSGS[name]
	if (!METAS[name]) throw `[${name}] is not loaded.`

	let msgs = {
		en: {},
		cn: {}
	}

	{
		let tmp = parseLocaleMsg(METAS[name].title)
		msgs.cn[name] = {
			title: tmp[0],
			group: {},
			field: {}
		}
		msgs.en[name] = {
			title: tmp[1],
			group: {},
			field: {}
		}
	}
	for (var i in METAS[name].fields) {
		let f = METAS[name].fields[i]
		if (!f.title) continue
		let tmp = parseLocaleMsg(f.title)
		msgs.cn[name].field[f.name] = tmp[0]
		msgs.en[name].field[f.name] = tmp[1]
		if (f.group) {
			tmp = parseLocaleMsg(f.group)
			msgs.cn[name].group[f.name] = tmp[0]
			msgs.en[name].group[f.name] = tmp[1]
		}
	}
	METAMSGS[name] = msgs[name]
	return msgs
}
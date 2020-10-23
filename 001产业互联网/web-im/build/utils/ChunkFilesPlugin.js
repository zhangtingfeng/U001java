"use strict";
const path = require('path');
const fse = require('fs-extra');

const DefaultOptions = {
  name: "entries.json",
  chunkNamePrefixLength: 0
};

class ChunkFilesPlugin {
  constructor(opts) {
    this.opts = Object.assign({}, DefaultOptions, opts || {})
  }

  apply(compiler) {
    var outputFolder = compiler.options.output.path;
    var outputFile = path.resolve(outputFolder, this.opts.name);

    compiler.hooks.emit.tap("ChunkFilesPlugin", compilation => {
      let chunkFiles = {}
      let indexName

      for (const chunk of compilation.chunks) {
        chunkFiles[chunk.id] = chunk.files[0].substr(this.opts.chunkNamePrefixLength)
        if (chunk.name === "index") {
          indexName = chunkFiles[chunk.id]
        }
      }

      chunkFiles.index = indexName
      fse.outputJsonSync(outputFile, chunkFiles)

    })
  }
}

module.exports = ChunkFilesPlugin;

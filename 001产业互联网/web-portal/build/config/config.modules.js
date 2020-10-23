import glob from 'glob'
import fs from 'fs'
import HtmlWebpackPlugin from 'html-webpack-plugin'

import {
    resolve,
    debug
} from "../utils"

let globalPath = './src/modules/**/index.js'

let isDev = debug()
let config = {
    entry: {},
    plugins: []
}

glob.sync(globalPath).forEach(function (entry) {
    let templateName = entry.substr(0, entry.length - 2) + "html"

    // 如果html文件不存在，则跳过
    if (!fs.existsSync(resolve(templateName))) {
        return
    }

    if (!debug() && /^\.\/src\/modules\/test/.test(templateName)) {
        return;
    }

    console.log(templateName)

    let tmp = entry.split('/')
    let name = ""
    for (let i = 3; i < tmp.length - 1; i++) {
        if (i > 3) {
            name += "/";
        }
        name += tmp[i];
    }

    let conf = {
        filename: name + '.html',
        template: templateName, // 模板路径
        inject: true, // js插入位置
        minify: {
            removeComments: !isDev,
            collapseWhitespace: !isDev,
            removeAttributeQuotes: !isDev
        },
        chunks: [].concat(name, "apps/runtime", "common", "vendors"),
        chunksSortMode: 'auto'
    }

    config.entry[name] = [entry]
    config.plugins.push(new HtmlWebpackPlugin(conf))
})

export default config
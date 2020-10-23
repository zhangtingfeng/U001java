import HtmlWebpackPlugin from 'html-webpack-plugin'

import {
    resolve,
    debug
} from "../utils"
import apps from "../../src/apps.json"
import appSetting from "../../appsetting"
let appName = appSetting.name

let isDev = debug()
let entry = resolve("./build/test/apps.js")

let config = {
    entry: { index: entry },
    module: {
        rules: [
            {
                test: entry,
                exclude: /(node_modules|bower_components)/,
                use: [
                    {
                        loader: 'babel-loader',
                        options: {
                            cacheDirectory: true
                        }
                    },
                    {
                        loader: resolve("./build/loaders/AppsLoader.js"),
                        options: {
                            name: appName,
                            apps
                        }
                    }
                ]
            }
        ]
    }
}
// 开发时增加测试页面
if (isDev) {
    config.entry.test = "./build/test/index.js";
    config.plugins = [new HtmlWebpackPlugin({
        filename: "index.html",
        template: resolve("./build/test/index.html"), // 模板路径
        inject: true
    })]
}

export default config
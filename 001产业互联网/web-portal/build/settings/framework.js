import fs from "fs"
import webpack from "webpack"

import {
    resolve,
    debug,
    buildName,
    HtmlInject
} from "../utils"
import appSetting from "../../appsetting"
let isDev = debug()

function linkFramework() {
    if (!fs.existsSync(resolve(`hs-libs`))) {
        fs.mkdirSync(resolve(`hs-libs`))
    }
    let n = buildName()
    if (!fs.existsSync(resolve(`hs-libs/${n}`))) {
        if (!fs.existsSync(resolve(`${appSetting.modules.framework}/${n}`))) {
            throw `${appSetting.modules.framework}/${n} is not exists.`
        }
        fs.symlinkSync(resolve(`${appSetting.modules.framework}/${n}`), resolve(`hs-libs/${n}`), "dir")
    }

    fs.writeFileSync(resolve("hs-libs/vendor.js"), "//for build")
    fs.writeFileSync(resolve("hs-libs/framework.js"), "//for build")
}

export function proc(config) {
    config.resolve.alias['@f'] = resolve('hs-libs')
    linkFramework()

    config.plugins.push(
        new webpack.DllReferencePlugin({
            manifest: require(resolve(`./hs-libs/${isDev ? "debug" : "release"}/vendor/manifest_vendor.json`))
        }))
    config.plugins.push(new webpack.DllReferencePlugin({
        manifest: require(resolve(`./hs-libs/${isDev ? "debug" : "release"}/framework/manifest_framework.json`))
    }))
    if (isDev) {
        config.plugins.push(new HtmlInject({
            version: appSetting.version,
            js: [
                "../vendor/vendor.js",
                "../framework/framework.js"
            ],
            css: [
                "../vendor/vendor.css",
            ]
        }))
    }
    return config
}
import webpack from "webpack"
import WebpackManifestPlugin from "webpack-manifest-plugin"

import {
    debug,
    buildName,
    resolve
} from "../utils"
let mode = debug() ? "development" : "production"

export default {
    mode,
    entry: {
        "framework": ["./hs-libs/framework.js"]
    },
    output: {
        path: resolve(`./dist/${buildName()}/framework/`),
        publicPath: "framework/",
        filename: "[name].js",
        library: "[name]"
    },
    resolve: {
        extensions: ['.js', '.json'],
        alias: {
            '@': resolve('src'),
            '@c': resolve('hs-libs/components'),
            '@f': resolve('hs-libs')
        }
    },
    plugins: [
        new WebpackManifestPlugin(),
        new webpack.DllReferencePlugin({
            manifest: require(resolve(`./dist/${buildName()}/vendor/manifest_vendor.json`))
        }),
        new webpack.DllPlugin({
            path: resolve(`dist/${buildName()}/framework/manifest_[name].json`),
            name: "[name]"
        })
    ],
    devtool: 'source-map',
    optimization: {
        // concatenateModules: true,
        // splitChunks: false
    }
}
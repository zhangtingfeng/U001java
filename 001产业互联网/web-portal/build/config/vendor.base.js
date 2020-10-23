import webpack from "webpack"
import WebpackManifestPlugin from "webpack-manifest-plugin"
import MiniCssExtractPlugin from "mini-css-extract-plugin"

import {
    debug,
    buildName,
    resolve
} from "../utils"
let mode = debug() ? "development" : "production"

export default {
    mode,
    entry: {
        "vendor": ["./hs-libs/vendor.js"],
    },
    output: {
        path: resolve(`./dist/${buildName()}/vendor/`),
        publicPath: "",
        filename: "[name].js",
        library: "[name]"
    },
    resolve: {
        extensions: ['.js', '.json'],
        alias: {
            '@': resolve('src'),
            '@f': resolve('hs-libs')
        }
    },
    plugins: [
        new WebpackManifestPlugin(),
        new webpack.DllPlugin({
            path: resolve(`dist/${buildName()}/vendor/manifest_[name].json`),
            name: "[name]"
        }),
        new MiniCssExtractPlugin({
            filename: "vendor.css"
        })
    ],
    devtool: 'source-map',
    optimization: {
        // concatenateModules: true,
        // splitChunks: false
    }
}
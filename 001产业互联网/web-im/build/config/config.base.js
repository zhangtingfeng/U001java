import webpack from "webpack"
import WebpackManifestPlugin from "webpack-manifest-plugin"

import {
    resolve,
    debug,
    buildName
} from "../utils"
import appSetting from "../../appsetting"
import ChunkFilesPlugin from "../utils/ChunkFilesPlugin"

let mode = debug() ? "development" : "production"
let appName = appSetting.name

if (!appName)
    throw `name should be setting in [appsetting]`

let config = {
    mode,
    output: {
        path: resolve(`./dist/${buildName()}`),
        filename: `${appName}/[name]-[hash:8].js`,
        chunkFilename: `${appName}/[name]-[contenthash:8].js`,
    },
    resolve: {
        extensions: ['.js', '.json'],
        alias: {
            '@': resolve('src')
        }
    },
    plugins: [
        new WebpackManifestPlugin(),
        new ChunkFilesPlugin({ name: `${appName}/entries.json`, chunkNamePrefixLength: appName.length + 1 })
        // new webpack.NamedModulesPlugin(),
        // new webpack.NamedChunksPlugin()
    ],
    devtool: 'source-map',
    optimization: {
        // namedModules: true,
        // namedChunks: true,
        concatenateModules: true,
        runtimeChunk: {
            name: "runtime"
        }
    }
}

if (debug()) {
    config.plugins = config.plugins.concat([
        new webpack.HotModuleReplacementPlugin(),
        new webpack.NoEmitOnErrorsPlugin(),
    ])
}

export default config

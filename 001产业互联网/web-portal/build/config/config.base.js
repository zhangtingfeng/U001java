import webpack from "webpack"
import WebpackManifestPlugin from "webpack-manifest-plugin"

import { resolve, debug, buildName, HtmlInject } from "../utils"
import appSetting from "../../appsetting"

let mode = debug() ? "development" : "production"

let config = {
	mode,
	output: {
		path: resolve(`./dist/${buildName()}`),
		filename: "[name]_[hash:8].js",
		chunkFilename: "apps/[name]_[contenthash:8].js",
	},
	resolve: {
		extensions: [".js", ".json"],
		alias: {
			"@": resolve("src"),
			"@c": resolve("hs-libs/components"),
			"@f": resolve("hs-libs"),
			"@m": resolve("src/modules"),
		},
	},
	plugins: [
		new WebpackManifestPlugin(),
		new HtmlInject({
			version: appSetting.version,
			js: ["../platform/vendor/vendor.js", "../platform/framework/framework.js"],
			css: ["../platform/vendor/vendor.css"],
		}),
	],
	devtool: "source-map",
	optimization: {
		concatenateModules: true,
		runtimeChunk: {
			name: "apps/runtime",
		},
		// splitChunks: false
		splitChunks: {
			chunks: "async",
			minSize: 30000,
			maxSize: 0,
			minChunks: 2,
			maxAsyncRequests: 5,
			maxInitialRequests: 3,
			automaticNameDelimiter: "_",
			name: true,
			cacheGroups: {
				vendors: {
					test: /[\\/]node_modules[\\/]/,
					chunks: "async",
					priority: -10,
					reuseExistingChunk: true,
					name: "vendors",
				},
				common: {
					chunks: "async",
					priority: -20,
					reuseExistingChunk: true,
					name: "common",
				},
			},
		},
	},
}

if (debug()) {
	config.plugins = config.plugins.concat([new webpack.HotModuleReplacementPlugin(), new webpack.NoEmitOnErrorsPlugin()])
}

export default config

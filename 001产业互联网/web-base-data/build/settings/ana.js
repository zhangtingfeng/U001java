import WebpackBundleAnalyzer from "webpack-bundle-analyzer"

export function proc(config) {
    config.plugins.push(new WebpackBundleAnalyzer.BundleAnalyzerPlugin({
        analyzerMode:"static"
    }))
    return config
}
import webpack from "webpack"

export function proc(config) {
    config.plugins.push(new webpack.ContextReplacementPlugin(
        /moment[/\\]locale$/,
        /zh-cn/
    ))
    return config
}
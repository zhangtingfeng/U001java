import webpack from "webpack"

// export default "jquery"

export function proc(config) {
    config.plugins.push(new webpack.ProvidePlugin({
        $: "jquery",
        jQuery: "jquery",
        'window.jQuery': "jquery"
    }))
    return config
}
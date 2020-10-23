export default function (name) {
    return [
        // "thread-loader",
        "style-loader",
        'css-loader',
        'postcss-loader'
    ].concat(name)
}

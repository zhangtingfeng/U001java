import MiniCssExtractPlugin from "mini-css-extract-plugin"

export default function (name, extract) {
    let loader0 = extract ? MiniCssExtractPlugin.loader : "style-loader"
    return [
        // "thread-loader",
        loader0,
        'css-loader',
        'postcss-loader'
    ].concat(name)
}
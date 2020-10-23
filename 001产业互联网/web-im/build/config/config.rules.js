import {
    styles
} from "../utils"

export default {
    module: {
        rules: [{
            test: /\.js$/,
            exclude: /(node_modules|bower_components)/,
            sideEffects: true,
            use: [
                // "thread-loader",
                'babel-loader'
            ]
        }, {
            test: /\.tsx?$/,
            exclude: /(node_modules|bower_components)/,
            use: [
                "thread-loader",
                'ts-loader'
            ]
        }, {
            test: /\.(sa|sc|c)ss$/,
            sideEffects: true,
            use: styles('sass-loader')
        },
        {
            test: /\.less$/,
            sideEffects: true,
            use: styles('less-loader')
        },
        {
            test: /\.(bmp|png|svg|jpg|gif)$/,
            use: [{
                loader: 'file-loader',
                options: {
                    limit: 1024,
                    name: "imgs/[name].[ext]",
                }
            }]
        }, {
            test: /\.(woff2?|eot|ttf|otf|svg)(\?.*)?$/,
            use: [
                "thread-loader",
                {
                    loader: 'url-loader',
                    options: {
                        limit: 1024,
                        name: 'fonts/[name].[ext]'
                    }
                }
            ]
        }]
    }
}
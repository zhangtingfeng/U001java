import VueLoader from "vue-loader"
import webpackMerge from "webpack-merge"
let vueConfig = {
    resolve: {
        extensions: ['.vue'],
        alias: {
            'vue': 'vue/dist/vue.esm.js'
        }
    },
    module: {
        rules: [{
            test: /\.vue$/,
            loader: ['thread-loader', 'vue-loader']
        },
        {
            resourceQuery: /blockType=i18n/,
            type: 'javascript/auto',
            loader: ['@intlify/vue-i18n-loader', 'yaml-loader'],
        }
        ]
    },
    plugins: [
        new VueLoader.VueLoaderPlugin(),
    ]
}

export function proc(config) {
    return webpackMerge(config, vueConfig)
}

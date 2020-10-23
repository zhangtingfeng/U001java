import express from 'express'
import webpack from 'webpack'
import webpackDevMiddleware from 'webpack-dev-middleware'
const proxyMidware = require('http-proxy-middleware')
const hotMidware = require('webpack-hot-middleware')
const hotMiddlewareScript = 'webpack-hot-middleware/client?path=/__webpack_hmr&timeout=10000&reload=true';

const config = require("./config").default

import {
    dev, name as appName
} from "../appsetting"
import { buildName } from "./utils"

export default () => {
    for (let n in config.entry) {
        if (!config.entry.hasOwnProperty(n)) {
            continue
        }
        if (!Array.isArray(config.entry[n])) {
            config.entry[n] = [config.entry[n]]
        }
        config.entry[n].push(hotMiddlewareScript)
    }

    const app = express()
    const compiler = webpack(config)

    // CORS
    app.all('*', function (req, res, next) {
        res.header('Access-Control-Allow-Origin', '*');
        res.header('Access-Control-Allow-Headers', 'Content-Type, Content-Length, Authorization, Accept, X-Requested-With , yourHeaderFeild');
        res.header('Access-Control-Allow-Methods', 'PUT, POST, GET, DELETE, OPTIONS');

        if (req.method === 'OPTIONS') {
            res.send(200); //让options请求快速返回/
        }
        else {
            next();
        }
    });

    // location
    app.use(express.static('static'))
    app.use('/vendor', express.static(`hs-libs/${buildName()}/vendor`))
    app.use('/framework', express.static(`hs-libs/${buildName()}/framework`))
    app.use(`/${appName}/entries.json`, express.static(`dist/${buildName()}/${appName}/entries.json`))

    app.use(webpackDevMiddleware(compiler, {
        publicPath: '/',
        noInfo: false,
        hot: true,
        inline: true,
        stats: {
            colors: true
        }
    }))

    // 热更新
    app.use(hotMidware(compiler, {
        log: console.log
    }));

    // proxy
    for (let p of dev.proxy) {
        // console.log(p)
        let pathRewrite = {}
        for (let pr in p.path) {
            if (!p.path.hasOwnProperty(pr)) {
                continue
            }

            pathRewrite[pr] = p.path[pr]
        }
        app.use(p.url, proxyMidware({
            target: p.target,
            pathRewrite,
            changeOrigin: true,
            onProxyRes: function (proxyRes, req, res) {
                var cookies = proxyRes.headers['set-cookie'];
                var cookieRegex = /Path=\/.*/i;
                //修改cookie Path
                if (cookies) {
                    var newCookie = cookies.map(function (cookie) {
                        if (cookieRegex.test(cookie)) {
                            return cookie.replace(cookieRegex, 'Path=/');
                        }
                        return cookie;
                    });
                    //修改cookie path
                    delete proxyRes.headers['set-cookie'];
                    proxyRes.headers['set-cookie'] = newCookie;
                }
            }
        }))
    }

    return new Promise((s, f) => {
        console.log(`start at ${dev.port}`)
        // start dev server
        app.listen(dev.port, (err) => {
            if (err) {
                f("err");
            } else {
                console.log(`start server at port ${dev.port}`)
                s("success");
            }
        })
    })
}
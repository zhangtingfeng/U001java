import express from "express"
const proxyMidware = require("http-proxy-middleware")

import { dev } from "../appsetting"

const app = express()
// location
app.use(express.static("dist/release"))

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
	app.use(
		p.url,
		proxyMidware({
			target: p.target,
			pathRewrite,
			changeOrigin: true,
			onProxyRes: function(proxyRes, req, res) {
				var cookies = proxyRes.headers["set-cookie"]
				var cookieRegex = /Path=\/.*/i
				//修改cookie Path
				if (cookies) {
					var newCookie = cookies.map(function(cookie) {
						if (cookieRegex.test(cookie)) {
							return cookie.replace(cookieRegex, "Path=/")
						}
						return cookie
					})
					//修改cookie path
					delete proxyRes.headers["set-cookie"]
					proxyRes.headers["set-cookie"] = newCookie
				}
			},
		})
	)
}

console.log(`start at ${dev.port}`)
// start host server
app.listen(dev.port, (err) => {
	if (err) {
		console.log("err")
	} else {
		console.log(`start server at port ${dev.port}`)
	}
})

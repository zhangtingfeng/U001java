import $ from "jquery";

const methods = ["get", "post", "put", "delete"];
$.ajaxSetup({
	contentType: "application/json;charset=utf-8",
	dataType: "json",
	timeout: 10 * 60 * 1000
})

function prepareRequest(config, m) {
	if (typeof config === "string") {
		config = {
			url: config
		}
	}
	config.method = m
	config.data = config.data ? JSON.stringify(config.data) : (/^get$/ig.test(m) ? "" : "{}")
	if (config.param) {
		config.url += (/\?/.test(config.url) ? '&' : '?') + $.param(config.param)
	}
	return config
}

// let ajax = {}
// for (const m of methods) {
// 	ajax[m] = (config) => {
// 		config = prepareRequest(config, m)
// 		let $d = $.Deferred()
// 		$.ajax(config).then((data) => {
// 			if (data.error)
// 				$d.reject(data.error)
// 			else
// 				$d.resolve(data)
// 		}, (xhr, status) => {
// 			if (xhr.status >= 200 && xhr.status < 300)
// 				$d.resolve({})
// 			else
// 				$d.reject(xhr, status)
// 		})
// 		return $d.promise()
// 	}
// }

let ajaxAsync = {}
for (const m of methods) {
	ajaxAsync[m] = (config) => {
		config = prepareRequest(config, m)
		return new Promise((resolve, reject) => {
			$.ajax(config).then((data) => {
				if (data.error)
					reject(data.error);
				else
					resolve(data)
			}, (xhr, status) => {
				if (xhr.status >= 200 && xhr.status < 300)
					resolve({})
				else
					reject(xhr, status);
			})
		})
	}
}

export default ajaxAsync

// export default ajax
// export {
// 	ajaxAsync
// }
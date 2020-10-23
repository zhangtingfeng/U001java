import apps from "@/apps/apps.json"
import { progress } from "@f/vendor"

// load all apps
let comps = {}
let loader = async (name, path) => {
	let THANDLE = window.setTimeout(() => {
		progress.show()
	}, 500)
	try {
		return await import(/* webpackChunkName: "app-[request]" */ `@/apps/${path}`)
	} catch (err) {
		console.error(err)
		return {
			template: `<p>[${name}] not exists</p>`,
		}
	} finally {
		window.clearTimeout(THANDLE)
		progress.hide()
	}
}
for (let a of apps) {
	let name = "App"
	a.split("/").forEach((app) => {
		name += app.substr(0, 1).toUpperCase() + app.substr(1)
	})

	comps[name] = () => loader(name, a)
}

let router = {
	comp: comps.AppMain,
	args: {},

	encodeArgs(args) {
		let rs = ""
		for (let n in args) {
			rs += n
			rs += "="
			rs += encodeURIComponent(args[n])
			rs += "&"
		}
	},
}

const AppPattern = /#!([^\?]+)\??(.*)$/
function routie() {
	let hash = decodeURIComponent(window.location.hash)
	let m = AppPattern.exec(hash)
	if (m) {
		if (comps[m[1]]) {
			router.comp = comps[m[1]]
			let args = {}
			let tmp = m[2].split("&")
			for (let t of tmp) {
				let t1 = t.split("=")
				if (t1[0]) {
					try {
						args[t1[0]] = decodeURIComponent(t1[1])
					} catch {
						args[t1[0]] = t1[1]
					}
				}
			}
			router.args = args
		}
	}
}

routie()
window.addEventListener("hashchange", (event) => {
	routie()
})

export default router

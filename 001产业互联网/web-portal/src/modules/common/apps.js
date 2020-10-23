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
	// for (let app of a.split('/')) {
	//     name += app.substr(0, 1).toUpperCase() + app.substr(1)
	// }

	comps[name] = () => loader(name, a)
}
export default comps

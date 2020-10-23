import menu from "./menu"

const MenuPattern = /#!menu\/(.+)$/
const AppPattern = /#!app\/(.+)$/
const ModulePattern = /#!m\/(.+)$/

function routieMenu(menuId) {
	let m = menu.get(menuId)
	if (
		m
		//&& menu.data.curr != menuId
	) {
		menu.data.component = { tempplate: "<h3>加载中......</h3>" }
		setTimeout(() => {
			menu.data.curr = menuId
			localStorage.setItem("platform-menu", menuId)
			routieApp(m.data.args, false)
		}, 100)
	}
}
function routieApp(app, prefix) {
	let m = /^([\w-]+)(\(([^\)]*)\))?(\?(.*))?$/.exec(app)
	if (m[1]) {
		let path = m[1]
		menu.data.component = `${prefix ? "App" : ""}${path.substr(0, 1).toUpperCase() + path.substr(1)}`
		menu.data.args = m[3] ? JSON.parse(m[3]) : m[3]
		loadMenu()
	}
}

function routieModule(app) {
	let m = /^([\w-]+)\/([\w-\/]+)(\?(.*))?$/.exec(app)
	if (m === null) return
	let args = {}
	if (m[4]) {
		for (let t of m[4].split("&")) {
			let t1 = t.split("=")
			if (t1[0]) args[t1[0]] = t1[1]
		}
	}
	menu.data.component = "ModuleLoader"
	menu.data.args = { app: [m[1], m[2]], args }
	loadMenu()
}

function loadMenu() {
	if (!menu.data.curr) {
		menu.data.curr = localStorage.getItem("platform-menu")
	}
}

function routie() {
	let hash = decodeURIComponent(window.location.hash)
	let m = MenuPattern.exec(hash)
	if (m) {
		routieMenu(m[1].split("?")[0])
		return
	}

	m = AppPattern.exec(hash)
	if (m) {
		routieApp(m[1], true)
		return
	}

	m = ModulePattern.exec(hash)
	if (m) {
		routieModule(m[1])
		return
	}
}

export default () => {
	routie()
	window.addEventListener("hashchange", (event) => {
		routie()
	})
}

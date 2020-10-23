import ajax from "@c/ajax"

const METAS = {}
const METAMSGS = {}

function parseLocaleMsg(msg) {
	let tmp = msg.split('|')
	return [tmp[0], tmp[1] ? tmp[1] : tmp[0]]
}

export default {
	reset(name) {
		let meta = METAS[name] = undefined
		let $d = $.Deferred()
		$d.resolve(meta)
		return $d.promise()
	},
	getPathName(name) {
		let t = name.lastIndexOf("/")
		let n = name.substr(t + 1)
		let p = name.substr(0, t + 1)
		return [p, n]
	},
	get(name) {
		let meta = METAS[name]

		// init
		if (undefined == meta) {
			let $d = $.Deferred()
			let promise = METAS[name] = $d.promise()
			let pathName = this.getPathName(name)
			ajax.get(`api/${pathName[0]}meta/${pathName[1]}`).then(data => {
				if (data.meta) {
					data.meta.pathName = pathName
					data.meta.name2 = pathName.join("")
					data.meta.url = `api/${pathName[0]}meta/${pathName[1]}`
					METAS[data.meta.name2] = data.meta
					if (data.meta.children) {
						for (let c in data.meta.children) {
							let m = data.meta.children[c]
							m.pathName = [pathName[0], m.name]
							m.name2 = m.pathName.join("")
							m.url = `api/${m.pathName[0]}meta/${m.pathName[1]}`
							METAS[m.name2] = m
						}
					}
					$d.resolve(METAS[name])
				} else
					$d.reject(2)
			}).catch((err) => {
				console.error(err)
				$d.reject(1)
			})
			return promise
		}

		// loading
		if (meta.then && typeof (meta.then) == "function")
			return meta

		// loaded
		{
			let $d = $.Deferred()
			$d.resolve(meta)
			return $d.promise()
		}
	},
	msgs(name) {
		if (METAMSGS[name]) return METAMSGS[name]
		if (!METAS[name]) throw `[${name}] is not loaded.`

		let msgs = {
			en: {},
			cn: {}
		}

		{
			let tmp = parseLocaleMsg(METAS[name].title)
			msgs.cn[name] = {
				title: tmp[0],
				group: {},
				field: {}
			}
			msgs.en[name] = {
				title: tmp[1],
				group: {},
				field: {}
			}
		}
		for (var i in METAS[name].fields) {
			let f = METAS[name].fields[i]
			if (!f.title) continue
			let tmp = parseLocaleMsg(f.title)
			msgs.cn[name].field[f.name] = tmp[0]
			msgs.en[name].field[f.name] = tmp[1]
			if (f.group) {
				tmp = parseLocaleMsg(f.group)
				msgs.cn[name].group[f.name] = tmp[0]
				msgs.en[name].group[f.name] = tmp[1]
			}
		}
		METAMSGS[name] = msgs[name]
		return msgs
	}
}
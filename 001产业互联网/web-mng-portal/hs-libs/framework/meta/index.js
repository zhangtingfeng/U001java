import metaRepo from "./meta"
import Search from "./search"
import Export from "./export"
import Form from "./form"

function mixinData(meta, d, mixin) {
	let m
	if ("function" === typeof (mixin)) {
		m = new mixin(meta)
	}
	else {
		m = mixin
		if (m.init) m.init(meta)
	}
	m["$data"] = d
	let name = m.name || mixin.name
	if (name) d[`_${name}`] = m
	for (let n in m) {
		if (n === "prototype" || n === "constructor" || n === "__proto__") {
			continue
		}
		if (typeof m[n] === "function") {
			d[n] = (...args) => {
				return m[n].apply(d, args)
			}
		} else {
			d[n] = m[n]
		}
	}
}

export default {
	init(meta, ...mixins) {
		let d = new Object()
		for (let mixin of mixins) {
			mixinData(meta, d, mixin)
		}
		return d
	},
	reset: name => metaRepo.reset(name),
	get: name => metaRepo.get(name),
	msgs: name => metaRepo.msgs(name)
}

export let Datas = { Search, Export, Form }
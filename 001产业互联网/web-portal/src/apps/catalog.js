import { ajax } from "@f/vendor"

const PROPS = [
	{
		id: "307E000004",
		name: "苹果产地",
		datas: [
			{ id: "山东" },
			{ id: "陕西" },
			{ id: "山西" },
			{ id: "甘肃" },
			{ id: "新疆" },
			{ id: "河北" },
			{ id: "河南" },
			{ id: "云南" },
			{ id: "四川" },
			{ id: "辽宁" },
			{ id: "江苏" },
			{ id: "其他" },
		],
	},
	{
		id: "P207P00001",
		name: "果径",
		datas: [
			{ id: "70mm以下" },
			{ id: "70mm以上" },
			{ id: "75mm以上" },
			{ id: "80mm以上" },
			{ id: "85mm以上" },
			{ id: "90mm以上" },
		],
	},
	{
		id: "307E000002",
		name: "色度/红度",
		datas: [{ id: "90%/80%" }, { id: "80%/70%" }, { id: "70%/60%" }],
	},
	{
		id: "P207P00005",
		name: "果型",
		datas: [{ id: "高桩" }, { id: "中高桩" }, { id: "中桩" }],
	},
	{
		id: "307E000003",
		name: "货品包装",
		datas: [{ id: "纸箱" }, { id: "塑料筐" }, { id: "大铁框" }],
	},
	{
		id: "P207P00007",
		name: "储存方式",
		datas: [{ id: "冷风" }, { id: "气调" }, { id: "预冷" }],
	},
]

export default {
	async loadCatalogs() {
		let data = await ajax.post({
			url: "api/basedata/basedata/classInfo.json",
		})
		var classes = []
		var id
		for (var info of data.classInfo) {
			if (info.pid == "#") {
				id = info.id
			}
		}
		for (var info of data.classInfo) {
			if (info.pid != "#") {
				if (info.pid == id) {
					info.degree = 1
				}
				classes.push(info)
			}
		}
		return classes
	},
	async loadCatalogTree() {
		let data = await ajax.post({
			url: "api/basedata/basedata/classInfo.json",
		})
		let root = null
		let clzObj = {}
		let classes = []
		for (let clz of data.classInfo) {
			clzObj[clz.id] = clz
			if (clz.pid === "#") {
				root = clz
			}
		}
		for (let clz of data.classInfo) {
			if (clz.pid === "#") {
				continue
			} else if (clz.pid === root.id) {
				classes.push(clz)
			} else {
				if (!clzObj[clz.pid].items) clzObj[clz.pid].items = []
				clzObj[clz.pid].items.push(clz)
			}
		}

		return classes
	},
	async loadProps(catalog) {
		let rs = []
		for (let p of PROPS) {
			let datas = []
			for (let d of p.datas) {
				datas.push({ id: d.id, active: false })
			}

			rs.push({ id: p.id, name: p.name, datas })
		}
		return rs
	},
}

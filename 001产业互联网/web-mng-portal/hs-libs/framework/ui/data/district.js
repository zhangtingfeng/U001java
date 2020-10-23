import dict from "../../dict"
let proviences = []
let datas = {}

function load() {
	let $d = $.Deferred()
	if (proviences.length) {
		$d.resolve(datas, proviences)
	} else {
		dict.getObjs("districts")
			.then(data => {
				datas = data
				for (let f in data) {
					let d = data[f]
					if (!f || !d)
						continue
					let dist = f.substr(4, 2);
					let c = f.substr(2, 2);
					let p = f.substr(0, 2);

					if (c == "00" && dist == '00') {
						proviences.push(d)
					} else {
						if ("00" == dist) {
							let provience = data[`${p}0000`]
							if (!provience.datas) provience.datas = []
							provience.datas.push(d)
						} else {
							let city = data[`${p}${c}00`]
							if (!city) 
								continue
							if (!city.datas) city.datas = []
							city.datas.push(d)
						}
					}
				}
				$d.resolve(datas, proviences)
			})
			.fail((e) => {
				$d.reject(e)
			})
	}
	return $d.promise()
}

export default {
	load
}
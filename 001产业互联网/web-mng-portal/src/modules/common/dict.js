import { dict } from "@f/framework"

// 数据字典
dict.set("dict-catalog", "api/basedata/dict/dict-catalog")

dict.get("dict-catalog").then(data => {
    for (let d of data)
        dict.set(d.id, `api/basedata/dict?name=${d.id}`)
})

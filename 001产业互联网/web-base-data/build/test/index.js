import { i18n } from "@f/framework"
import Vue from 'vue'
import App from "./app"

import { dict } from "@f/framework"

// 数据字典
dict.set("dict-catalog", "api/basedata/dict/dict-catalog")

dict.get("dict-catalog").then(data => {
    for (let d of data)
        dict.set(d.id, `api/basedata/dict?name=${d.id}`)
})

new Vue({
    el: '#app',
    i18n,
    template: '<app/>',
    components: {
        App
    }
}) 
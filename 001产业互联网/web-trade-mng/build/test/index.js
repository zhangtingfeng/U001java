import { i18n } from "@f/framework"
import Vue from 'vue'
import App from "./app"

import { dict } from "@f/framework"

async function init() {
    // 数据字典
    dict.set("dict-catalog", "api/basedata/dict/dict-catalog")
    try {
        let data = await dict.get("dict-catalog");
        for (let d of data)
            dict.set(d.id, `api/basedata/dict?name=${d.id}`)

    } catch (err) {
        console.error(err)
    }
    new Vue({
        el: '#app',
        i18n,
        template: '<app/>',
        components: {
            App
        }
    })
}

init();

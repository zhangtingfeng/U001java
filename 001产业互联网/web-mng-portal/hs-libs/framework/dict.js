import { ajax } from "@f/vendor"

const DICT = {
    // 数据字典配置
    configs: {},
    // 数据字典数
    datas: {},
    // 数据数组转对象
    dataObjs: {}
}

async function loadData(name, args) {
    let config = args

    let rs = await ajax.get(config)
    // return new Promise(r => {
    //     window.setTimeout(() => {
    //         r(Array.isArray(rs) ? rs : rs[name])
    //     }, 10000)
    // })
    return Array.isArray(rs) ? rs : rs[name]
}

export default {
    /**
     * 配置指定名称的数据字典
     * @param {*} name 数据字典的名称
     * @param {*} config 数据字典配置。如果是数组，直接作为数据字典的数据;如果字符串表示获取数据的url；如果是对象表示获取数据的ajax参数。
     */
    set(name, config) {
        if (Array.isArray(config)) {
            DICT.datas[name] = config
        } else {
            DICT.configs[name] = config
            delete DICT.datas[name]
        }

        delete DICT.dataObjs[name]
    },

    /**
     * 获取指定名称的数据字典数据数组
     * @param {*} name 
     */
    async get(name) {
        let data = DICT.datas[name]

        // 如果不存在
        if (!data) {
            let config = DICT.configs[name]
            // 如果没配置，抛出异常
            if (!config) {
                throw `dict [${name}] is not defined.`
            }

            // 取数据
            DICT.datas[name] = new Promise(async (resolve, reject) => {
                try {
                    let d = await loadData(name, config)
                    DICT.datas[name] = d
                    resolve(d)
                } catch (e) {
                    reject(e)
                }
            })

            return DICT.datas[name]
        }

        return data
    },

    /**
     * 获取指定名称的数据字典对象,以id字段为field name
     * @param {*} name 
     */
    async getObjs(name) {
        let obj = DICT.dataObjs[name]
        if (!obj) {
            obj = {}
            let datas = await this.get(name)
            datas.forEach(d => {
                obj[d.id] = d
            })
            DICT.dataObjs[name] = obj
        }

        return obj
    },

    /**
     * 重新加载dict
     * @param  {string} name 
     */
    reset(name) {
        delete DICT.datas[name]
        delete DICT.dataObjs[name]
    }
}
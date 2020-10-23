import fs from 'fs'
import {
    logger,
    resolve
} from "../utils"
import * as appSetting from "../../appsetting"

let settingEntries = []
let settingProcs = []

for (let n in appSetting.modules) {
    if (!appSetting.modules[n]) {
        continue
    }

    // logger.debug(`enable module:[${n}]`)
    let s = resolve(`build/settings/${n}.js`)
    if (fs.existsSync(s)) {
        let so = require(s)

        if (!so)
            continue

        if (so.default) {
            let se = Array.isArray(so.default) ? so.default : [so.default]
            settingEntries = settingEntries.concat(se)
        }

        if (so.proc) {
            settingProcs.push(so.proc)
        }
    }
}

export default function (config) {
    for (let name in config.entry) {
        let m = config.entry[name]
        if (!Array.isArray(m)) {
            m = [m]
        }
        config.entry[name] = m.concat(settingEntries)
    }

    settingProcs.forEach(p => {
        let c = p(config)
        if (c)
            config = c
    })

    return config
}
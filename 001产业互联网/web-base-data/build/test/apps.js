import appsConfig from "@/apps.json"
import { apps } from "@f/framework"
import appSetting from "@/../appsetting.json"

let comps = {}
for (let app of appsConfig) {
    comps[app] = () => import(`@/${app}`)
}
apps.set(appSetting.name, comps)

export default comps
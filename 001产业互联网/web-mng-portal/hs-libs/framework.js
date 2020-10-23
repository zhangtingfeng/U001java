import "./vendor.js"
import './components/validate'
import locale, {
	i18n
} from "./components/locale"
import script from "./components/script"
import comps1 from "./components/ui"
import comps2 from "./framework/ui"
import Table1 from "./framework/ui/Table1"
import Table1ztf from "./framework/ui/Table1ztf"
import TableX from "./framework/ui/TableX"
import ModuleLoader from "./framework/ui/loader/index.vue"
import editors from "./framework/editors"
import views from "./framework/views"
import dict from "./framework/dict"
import apps from "./framework/apps"
import { user, auths } from "./framework/user"
import meta, { Datas } from "./framework/meta"

// views
const VIEWERS = [
	"bool",
	"dict",
	"date",
	"time",
	"datetime",
	"district"
]

// editors
const EDITORS = [
	"date",
	"bool",
	"password",
	"select",
	"selectM",
	"datetime",
	"time",
	"textarea",
	"audio",
	"upload",
	"district"
];

VIEWERS.forEach(n => {
	views.set(n, () => import(
		/* webpackInclude: /\.vue/ */
		/* webpackChunkName: "views-[request]" */
		`@f/framework/ui/views/${n}`))
})
EDITORS.forEach(n => {
	editors.set(n, () => import(
		/* webpackInclude: /\.vue/ */
		/* webpackChunkName: "editors-[request]" */
		`@f/framework/ui/editors/${n}`))
})

export default { Table1, Table1ztf,TableX, ModuleLoader }

let comps = Object.assign({}, comps1, comps2)

export {
	locale,
	i18n,
	script,
	editors,
	views,
	dict,
	apps,
	meta,
	Datas as MetaDatas,
	user,
	auths,
	comps,
	Table1,
	Table1ztf,
	TableX
}

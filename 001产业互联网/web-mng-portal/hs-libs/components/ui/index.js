const COMPS = ["FileUpload", "Checkbox", "ChgLocale", "Date", "Radiobox", "Select2", "Tree"]
let obj = {};

COMPS.forEach(n => {
    obj[n] = (() => import(
        /* webpackExclude: /\.js/ */
        /* webpackChunkName: "comps-[request]" */
        `./${n}`
    ))
})

export default obj
export let Checkbox = obj.Checkbox
export let ChgLocale = obj.ChgLocale
export let Date = obj.Date
export let Radiobox = obj.Radiobox
export let Select2 = obj.Select2
export let Tree = obj.Tree
export let FileUpload = obj.FileUpload

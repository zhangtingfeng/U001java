import fs from "fs"
import { getOptions } from 'loader-utils';
import { resolve } from "../utils"

module.exports = function (content) {
  const options = getOptions(this)
  let rs = `
import { apps } from "@f/framework"
const comps = {`
  for (let app of options.apps) {
    let n = app.replace(/\//g, "-")
    rs += `
    "${app}": ()=>import(/* webpackChunkName: "app_${n}" */"@/apps/${app}"),`
  }

  rs += `
}

apps.set("${options.name}", comps)

export default comps
`

  if (fs.existsSync(resolve("./src/index.js"))) {
    rs += `
import "@/index"
    `
  }

  console.log(rs);

  return rs;
}
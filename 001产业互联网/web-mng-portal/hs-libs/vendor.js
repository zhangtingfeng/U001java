// Jquery
import jQuery from "jquery"
// Bootstrap
import "bootstrap"
import "./assets/bootstrap.less"
import "admin-lte"
import "./assets/admin-lte.less"

import "font-awesome/less/font-awesome.less"

import "./assets/vendor.less"

// vue
import Vue from "vue"
// Vue.config.productionTip = false
Vue.config.devtools = true

import "@f/components/extend"
import "./assets/themes.less"

jQuery("body").addClass("skin-blue-light")

import ajax from "@f/components/ajax"
import msg from "@f/components/msg"
import progress from "@f/components/progress"

export { ajax, msg, progress }

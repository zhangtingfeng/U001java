import { i18n } from "@f/framework"
import Vue from "vue"
import "@c/validate"
import "@c/focus"
import "@m/common"
import Login from './Login'

new Vue({
    el: "#app",
    i18n,
    template: '<login/>',
    components: {
        Login
    }
})
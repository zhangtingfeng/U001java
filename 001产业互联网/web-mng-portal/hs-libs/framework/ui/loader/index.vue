<template>
  <comp :is="comp" v-if="comp" :app="app" v-bind="args" />
</template>

<script>
import progress from "@f/components/progress";
import script from "@f/components/script";
import dict from "../../dict";
import apps from "../../apps";
import ChunkLoader from "./loader";

const SysModules = "sys-modules";
dict.set(SysModules, "api/basedata/dict/sys-modules");

export default {
  props: ["app", "args"],
  data() {
    return {
      comp: null,
    };
  },
  watch: {
    app() {
      this.comp = null;
      this.init();
    },
    args() {
      this.comp = null;
      this.init();
    },
  },
  methods: {
    async init() {
      // let ProgressHandle = setTimeout(() => {
      //   progress.show();
      // }, 100);
      try {
        let modulesData = await dict.getObjs(SysModules);
        let moduleData = modulesData[this.app[0]];
        if (!moduleData) throw `Module[${this.app[0]}] is not defined.`;
        let chunk = await ChunkLoader(moduleData);

        let comp = apps.get(this.app[0], this.app[1]);
        if (!comp) throw `${this.app[0]} - ${this.app[1]} is not exists`;

        if ("function" === typeof comp) {
          comp = await comp();
        }

        this.comp = comp.default || comp;
      } catch (e) {
        this.comp = {
          template: `<h3>${e}</h3>`,
        };
      } finally {
        // clearTimeout(ProgressHandle);
        // progress.hide();
      }
    },
  },
  mounted() {
    this.init();
  },
};
</script>

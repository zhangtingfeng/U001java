<template>
  <chunk-loader :app="app" />
</template>

<script>
import framework, { dict } from "@f/framework";

// dict.set("sys-modules", [
//   { id: "test1", url: "http://localhost:92/test1/", version: "1.0" }
// ]);

export default {
  components: { ChunkLoader: framework.ModuleLoader },
  data() {
    return { app: ["test1", "test1"] };
  },
  methods: {
    async init() {}
  },
  mounted() {
    this.init();
  }
};
</script>

<template>
  <div class="input-group">
    <div class="district">
      <span v-if="state == -1">
        {{$t("error.title")}}
      </span>
      <template v-if="state == 1">
        <div>
          <select2 :options="proviences" :readonly="readonly" width="100%" hint="省" v-model="provience" />
        </div>
        <div>
          <select2 :options="cities" :readonly="readonly" width="100%" hint="市" v-model="city" />
        </div>
        <div>
          <select2 :options="districts" :readonly="readonly" width="100%" hint="区" v-model="district" />
        </div>
      </template>
    </div>
  </div>
</template>

<script>
import select2 from "@c/ui/Select2";
import district from "../data/district";

export default {
  components: {
    select2
  },
  data() {
    let provience = this.value ? this.value.substr(0, 2) + "0000" : "";
    let city = this.value ? this.value.substr(0, 4) + "00" : "";
    let district = this.value;
    return {
      datas: {},
      proviences: [],
      cities: [],
      districts: [],
      provience,
      city,
      district,
      state: 0
    };
  },
  props: ["value", "readonly"],
  watch: {
    provience(val) {
      this.city = "";
      if (val) this.cities = this.datas[val].datas;
      this.district = "";
      this.districts = [];
    },
    city(val) {
      this.district = "";
      if (val) this.districts = this.datas[val].datas;
    },
    district(val) {
      this.$emit("input", val);
    }
  },
  methods: {
    load() {
      let self = this;
      district
        .load()
        .then((datas, proviences) => {
          self.datas = datas;
          this.proviences = proviences;
          if (this.value) {
            this.cities = datas[this.provience].datas;
            this.districts = datas[this.city].datas;
          }
          this.state = 1;
        })
        .fail(e => {
          this.state = -1;
        });
    }
  },
  mounted() {
    this.load();
  }
};
</script>

<style scoped>
.district {
  display: flex;
  flex-flow: row nowrap;
}
.district div {
  min-width: 100px;
  flex: 0 0 auto;
}
</style>

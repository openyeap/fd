<template>
  <div class="home">
    <h1>this is micro application two</h1>
    <img alt="Vue logo" src="../assets/logo.png" />
    <router-link to="/app1/about">关于应用1</router-link>
    <HelloWorld msg="Welcome to Your Vue.js App" />
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from "@/components/HelloWorld.vue";

export default {
  name: "home",
  components: {
    HelloWorld,
  },
};
</script>

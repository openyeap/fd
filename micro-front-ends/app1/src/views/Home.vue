<template>
  <div class="home">
    <h1>this is micro application one</h1>
    <img alt="Vue logo" src="../assets/logo.png" />
    <router-link to='app2/about'>关于应用2</router-link>
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

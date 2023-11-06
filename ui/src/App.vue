<template>
    <div class="app">
        <game/>
    </div>
        
</template>

<script>
import Game from "@/components/Game";
    
export default {
    components: {
         Game
    },
    data() {
        return {
            answers:[[]],
            
        }
    },
    
    methods: {
        choice(answer) {
            this.answers.push(answer);
        }, 
        
        
    },
    
}
</script>
<style>
* {
    margin: 0;
    padding: 0;
    
}

</style>
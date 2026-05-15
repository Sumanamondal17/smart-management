console.log("script loaded")

let currentTheme=getTheme();

changeTheme()
//todo
function changeTheme(){
    console.log(currentTheme);
    document.querySelector("html").classList.add(currentTheme);
    const changeThemeButton=document.querySelector('#theme_change_button')
    changeThemeButton.addEventListener('click',(event)=>{
        console.log("change theme button clicked")
        const oldTheme=currentTheme;
        if(currentTheme==="dark"){
            currentTheme="light"
        }else{
            currentTheme="dark"
        }
        setTheme(currentTheme);
        document.querySelector('html').classList.remove(oldTheme);

        document.querySelector('html').classList.add(currentTheme);

    })
}
//set theme to localstorage
function setTheme(theme){
    localStorage.setItem("theme",theme);
}
//get theme to localstorage
function getTheme(){
    let theme=localStorage.getItem("theme");
    if(theme){
        return theme;
    }else{
        return "light";

    }
}
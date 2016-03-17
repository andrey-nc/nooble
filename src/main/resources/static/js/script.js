function showValue(newValue){
    document.getElementById("rangeValue").innerHTML = newValue;
}

function showStatus(value){
    clearStatus();
    document.getElementById("status").innerHTML = value;
}

function clearStatus(){
    document.getElementById("status").innerHTML = "";
}
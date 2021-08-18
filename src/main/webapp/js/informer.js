var opCss = document.createElement('link');
opCss.setAttribute('rel', 'stylesheet');
opCss.setAttribute('type', 'text/css');
opCss.setAttribute('href', document.getElementById('Oplao').getAttribute('data-url')+'/scss/widget.css');
document.getElementsByTagName('head')[0].appendChild(opCss);
function LoadInformer(data) {
    if (document.getElementById('opTitle')) {
        document.getElementById('opTitle').innerHTML = data.city.toLocaleUpperCase()+', '+data.countryCode.toLocaleUpperCase()
    }
    if (document.getElementById('opImg')) {
        var day = data.hours<=6 || data.hours>=18?'night':'day'
        var url = document.getElementById('Oplao').getAttribute('data-url')+"/svg/wicons_svg/"+data.weatherIconCode+"_"+day+".svg"

        document.getElementById('opImg').src = url
        document.getElementById('opImg').style.height = 'auto!important'
    }
    if (document.getElementById('opTemp')) {
        var temp = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? 'F' : 'C';
        var tempNum = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? data.temp_f : data.temp_c;
        document.getElementById('opTemp').innerHTML = "<span>"+tempNum+"</span><sup>°"+temp+"</sup>"
    }
    if (document.getElementById('opFeel')) {
        var opFeel = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? 'F' : 'C';
        var opFeelNum = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? data.feelsLikeF : data.feelsLikeC;
        document.getElementById('opFeel').innerHTML = "<span>"+opFeelNum+"</span><sup>°"+opFeel+"</sup>"
    }

    if (document.getElementById('opClarity')) {
        document.getElementById('opClarity').innerHTML = data.clarity
    }
    if (document.getElementById('opPressure')) {
        var opPressure = document.getElementById('Oplao').getAttribute('data-pressure') == 'hPa' ? 'hPa' : 'in';
        var opPressureNum = document.getElementById('Oplao').getAttribute('data-pressure') == 'hPa' ? data.pressurehPa : data.pressureInch;
        document.getElementById('opPressure').innerHTML = "<span>"+opPressureNum+" "+opPressure+"</span>"
    }
    if (document.getElementById('opWind')) {
        var opWind = document.getElementById('Oplao').getAttribute('data-wind') == 'm/s' ? 'm/s' : 'mph';
        var opWindNum = document.getElementById('Oplao').getAttribute('data-wind') == 'm/s' ? data.windMs : data.windMph;
        document.getElementById('opWind').innerHTML = '<img class="wind_icon" src="'+document.getElementById('Oplao').getAttribute('data-url')+'"/img/widget/wg_wind.png" style="-ms-transform: rotate('+(data.windDegree+20)+'deg); -webkit-transform: rotate('+(data.windDegree+20)+'deg);transform: rotate('+(data.windDegree+20)+'deg);" alt=""><span> '+opWindNum+' '+opWind+'</span>'
    }
    if (document.getElementById('threeDays')) {
        var day = data.hours<=6 || data.hours>=18?'night':'day'
        var url1 = document.getElementById('Oplao').getAttribute('data-url')+"/svg/wicons_svg/"+data.threeDays[0].icon+"_"+day+".svg"
        var url2 = document.getElementById('Oplao').getAttribute('data-url')+"/svg/wicons_svg/"+data.threeDays[1].icon+"_"+day+".svg"
        var url3 = document.getElementById('Oplao').getAttribute('data-url')+"/svg/wicons_svg/"+data.threeDays[2].icon+"_"+day+".svg"


        document.getElementById('opDate').innerHTML = data.threeDays[0].date
        document.getElementById('opDate2').innerHTML = data.threeDays[1].date
        document.getElementById('opDate3').innerHTML = data.threeDays[2].date
        document.getElementById('opDay').innerHTML = data.threeDays[0].day
        document.getElementById('opDay2').innerHTML = data.threeDays[1].day
        document.getElementById('opDay3').innerHTML = data.threeDays[2].day
        document.getElementById('opImg').src = url1
        document.getElementById('opImg2').src = url2
        document.getElementById('opImg3').src = url3
        document.getElementById('opClarity').innerHTML = data.threeDays[0].clarity
        document.getElementById('opClarity2').innerHTML = data.threeDays[1].clarity
        document.getElementById('opClarity3').innerHTML = data.threeDays[2].clarity


        var temp = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? 'F' : 'C';
        var tempNum = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? data.threeDays[0].temp_f : data.threeDays[0].temp_c;
        document.getElementById('opTemp').innerHTML = "<span>"+tempNum+"</span><sup>°"+temp+"</sup>"

        var temp2 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? 'F' : 'C';
        var tempNum2 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? data.threeDays[1].temp_f : data.threeDays[1].temp_c;
        document.getElementById('opTemp2').innerHTML = "<span>"+tempNum2+"</span><sup>°"+temp2+"</sup>"

        var temp3 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? 'F' : 'C';
        var tempNum3 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? data.threeDays[2].temp_f : data.threeDays[2].temp_c;
        document.getElementById('opTemp3').innerHTML = "<span>"+tempNum3+"</span><sup>°"+temp3+"</sup>"
    }

    if (document.getElementById('dayThreeDays')) {
        var day = data.hours<=6 || data.hours>=18?'night':'day'
        var url1 = document.getElementById('Oplao').getAttribute('data-url')+"/svg/wicons_svg/"+data.threeDays[0].icon+"_"+day+".svg"
        var url2 = document.getElementById('Oplao').getAttribute('data-url')+"/svg/wicons_svg/"+data.threeDays[1].icon+"_"+day+".svg"
        var url3 = document.getElementById('Oplao').getAttribute('data-url')+"/svg/wicons_svg/"+data.threeDays[2].icon+"_"+day+".svg"

        document.getElementById('opDate').innerHTML = data.date
        document.getElementById('opDay').innerHTML = data.day
        document.getElementById('opDate1').innerHTML = data.threeDays[0].date
        document.getElementById('opDate2').innerHTML = data.threeDays[1].date
        document.getElementById('opDate3').innerHTML = data.threeDays[2].date
        document.getElementById('opDay1').innerHTML = data.threeDays[0].day
        document.getElementById('opDay2').innerHTML = data.threeDays[1].day
        document.getElementById('opDay3').innerHTML = data.threeDays[2].day
        document.getElementById('opImg1').src = url1
        document.getElementById('opImg2').src = url2
        document.getElementById('opImg3').src = url3
        document.getElementById('opClarity1').innerHTML = data.threeDays[0].clarity
        document.getElementById('opClarity2').innerHTML = data.threeDays[1].clarity
        document.getElementById('opClarity3').innerHTML = data.threeDays[2].clarity


        var temp1 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? 'F' : 'C';
        var tempNum1 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? data.threeDays[0].temp_f : data.threeDays[0].temp_c;
        document.getElementById('opTemp1').innerHTML = "<span>"+tempNum1+"</span><sup>°"+temp1+"</sup>"

        var temp2 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? 'F' : 'C';
        var tempNum2 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? data.threeDays[1].temp_f : data.threeDays[1].temp_c;
        document.getElementById('opTemp2').innerHTML = "<span>"+tempNum2+"</span><sup>°"+temp2+"</sup>"

        var temp3 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? 'F' : 'C';
        var tempNum3 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? data.threeDays[2].temp_f : data.threeDays[2].temp_c;
        document.getElementById('opTemp3').innerHTML = "<span>"+tempNum3+"</span><sup>°"+temp3+"</sup>"
    }

    if (document.getElementById('widget_4') || document.getElementById('widget_8') || document.getElementById('widget_9') || document.getElementById('widget_10')) {
        var day = data.hours<=6 || data.hours>=18?'night':'day'
        var url1 = document.getElementById('Oplao').getAttribute('data-url')+"/svg/wicons_svg/"+data.wholeDay[0].icon+"_"+day+".svg"
        var url2 = document.getElementById('Oplao').getAttribute('data-url')+"/svg/wicons_svg/"+data.wholeDay[1].icon+"_"+day+".svg"
        var url3 = document.getElementById('Oplao').getAttribute('data-url')+"/svg/wicons_svg/"+data.wholeDay[2].icon+"_"+day+".svg"
        var url4 = document.getElementById('Oplao').getAttribute('data-url')+"/svg/wicons_svg/"+data.wholeDay[3].icon+"_"+day+".svg"
        if (document.getElementById('opDate')){
            document.getElementById('opDate').innerHTML = data.date
        }
        if (document.getElementById('opTime')){
            document.getElementById('opTime').innerHTML = data.time
        }
        document.getElementById('opImg1').src = url1
        document.getElementById('opImg2').src = url2
        document.getElementById('opImg3').src = url3
        document.getElementById('opImg4').src = url4


        var temp1 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? 'F' : 'C';
        var tempNum1 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? data.wholeDay[0].temp_f : data.wholeDay[0].temp_c;
        document.getElementById('opTemp1').innerHTML = "<span>"+tempNum1+"</span><sup>°"+temp1+"</sup>"

        var temp2 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? 'F' : 'C';
        var tempNum2 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? data.wholeDay[1].temp_f : data.wholeDay[1].temp_c;
        document.getElementById('opTemp2').innerHTML = "<span>"+tempNum2+"</span><sup>°"+temp2+"</sup>"

        var temp3 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? 'F' : 'C';
        var tempNum3 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? data.wholeDay[2].temp_f : data.wholeDay[2].temp_c;
        document.getElementById('opTemp3').innerHTML = "<span>"+tempNum3+"</span><sup>°"+temp3+"</sup>"

        var temp4 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? 'F' : 'C';
        var tempNum4 = document.getElementById('Oplao').getAttribute('data-temp') == 'F' ? data.wholeDay[3].temp_f : data.wholeDay[3].temp_c;
        document.getElementById('opTemp4').innerHTML = "<span>"+tempNum4+"</span><sup>°"+temp4+"</sup>"
    }

    document.getElementById('Oplao').style.display = 'initial';
}

var xhttp = new XMLHttpRequest();
var params = "city="+document.getElementById('Oplao').getAttribute('data-city')+"&lang="+document.getElementById('Oplao').getAttribute('data-lang')
xhttp.onreadystatechange = function() {
    if (xhttp.readyState == 4 && xhttp.status == 200) {
        LoadInformer(JSON.parse(xhttp.responseText));
    }
}
xhttp.open("POST", document.getElementById('Oplao').getAttribute('data-url') + "/get_info_widgets?city="+document.getElementById('Oplao').getAttribute("data-city")+"&lang="+document.getElementById('Oplao').getAttribute('data-lang'));
xhttp.send(params);

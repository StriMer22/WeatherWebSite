//CALENDAR

$(function () {
console.log($('#calendar').length);
    if ($('#calendar').length) {
        if(!$.datepicker.initialized) {
            $.datepicker._updateDatepicker_original = $.datepicker._updateDatepicker;
        }
        $.datepicker._updateDatepicker = function (inst) {
            $.datepicker._updateDatepicker_original(inst);
            var afterShow = this._get(inst, 'afterShow');
            if (afterShow)
                afterShow.apply((inst.input ? inst.input[0] : null));  // trigger custom callback
        }
        $('#calendar').datepicker({
            inline: true,
            showOtherMonths: true,
            selectOtherMonths: true,
            changeMonth: true,
            changeYear: true,
            maxDate: new Date(new Date().getTime() + 13 * 24 * 60 *60 * 1000), //disabling date for 14 + days
            minDate: new Date("2008-07-01"),
            afterShow: function () {
                $(".ui-datepicker select").styler();

            },
            onSelect: function () {
                var scope = angular.element("[ng-controller=past-weatherCtrl]").scope();
                var selectedDate = $(this).datepicker('getDate');
                var year = selectedDate.getFullYear();
                var day = parseInt(selectedDate.getDate())<10?'0'+ selectedDate.getDate():selectedDate.getDate();
                var month = parseInt(selectedDate.getMonth()) + 1<10?'0'+ (parseInt(selectedDate.getMonth())+1):parseInt(selectedDate.getMonth()) + 1;

                scope.showWeatherForDate( year + "-" + month  + "-" + day);
                $('#dp-calendar').removeClass('visible-calendar');
            }
        });


        /*
         ** visible calendar
         */
        $("#calendar").click(function (e) {
            e.stopPropagation();
        });

        $('#dp-calendar .calendar-activator').on('click', function (e) {
            $('#dp-calendar').addClass('visible-calendar');
        });
        $('html,body').click(function (e) {
            //e.stopPropagation();

            if (!$(e.target).is(".calendar-activator") && !$(e.target).parents(".ui-datepicker").length && !$(e.target).is(".calendar-activator .img ") && !$(e.target).is(".calendar-activator span#pickADate") ) {
                $('#dp-calendar').removeClass('visible-calendar');
            }
        });
        $('.close-calendar').click(function (e) {
            //e.stopPropagation();

            if (!$(e.target).is(".calendar-activator") && !$(e.target).parents(".ui-datepicker").length) {
                $('#dp-calendar').removeClass('visible-calendar');
            }
        });
    }
});

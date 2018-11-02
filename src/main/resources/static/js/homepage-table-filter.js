$(document).ready(function () {
    var selectedOption = document.getElementById("selected_language").innerHTML;
    if (selectedOption.trim() === "et") {
        $('#candidateTable').DataTable({
            "info": false,
            "language": {"url": "https://cdn.datatables.net/plug-ins/1.10.19/i18n/Estonian.json"}
        });
    } else {
        $('#candidateTable').DataTable({
            "info": false
        });
    }
});
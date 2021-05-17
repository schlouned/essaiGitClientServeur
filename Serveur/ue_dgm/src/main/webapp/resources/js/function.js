/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function refreshMenu() {
//recover the title of the page
    var selectedPage = document.getElementById("detailLegend").innerText;

//recover the differents parts of the menu
    var indexMenu = document.getElementById("formMenu:indexHtml");
    var personMenu = document.getElementById("formMenu:personHtml");
    var sectionMenu = document.getElementById("formMenu:sectionHtml");
    var ueMenu = document.getElementById("formMenu:ueHtml");
    var ueOrgaMenu = document.getElementById("formMenu:organizedUeHtml");
    var planningMenu = document.getElementById("formMenu:planningHtml");
    var presenceMenu = document.getElementById("formMenu:presenceHtml");

//code
    if (selectedPage === "Gestion de l'ISL") {
        indexMenu.className = "indexButtonSelected";
        personMenu.className = "indexButton";
        sectionMenu.className = "indexButton";
        ueMenu.className = "indexButton";
        ueOrgaMenu.className = "indexButton";
        planningMenu.className = "indexButton";
        presenceMenu.className = "indexButton";
    }
    if (selectedPage === "Personne") {
        indexMenu.className = "indexButton";
        personMenu.className = "indexButtonSelected";
        sectionMenu.className = "indexButton";
        ueMenu.className = "indexButton";
        ueOrgaMenu.className = "indexButton";
        planningMenu.className = "indexButton";
        presenceMenu.className = "indexButton";
    }
    if (selectedPage === "Section") {
        indexMenu.className = "indexButton";
        personMenu.className = "indexButton";
        sectionMenu.className = "indexButtonSelected";
        ueMenu.className = "indexButton";
        ueOrgaMenu.className = "indexButton";
        planningMenu.className = "indexButton";
        presenceMenu.className = "indexButton";
    }
    if (selectedPage === "Unité d'enseignement") {
        indexMenu.className = "indexButton";
        personMenu.className = "indexButton";
        sectionMenu.className = "indexButton";
        ueMenu.className = "indexButtonSelected";
        ueOrgaMenu.className = "indexButton";
        planningMenu.className = "indexButton";
        presenceMenu.className = "indexButton";
    }
    if (selectedPage === "Ue Organises") {
        indexMenu.className = "indexButton";
        personMenu.className = "indexButton";
        sectionMenu.className = "indexButton";
        ueMenu.className = "indexButton";
        ueOrgaMenu.className = "indexButtonSelected";
        planningMenu.className = "indexButton";
        presenceMenu.className = "indexButton";
    }
    if (selectedPage === "Planning") {
        indexMenu.className = "indexButton";
        personMenu.className = "indexButton";
        sectionMenu.className = "indexButton";
        ueMenu.className = "indexButton";
        ueOrgaMenu.className = "indexButton";
        planningMenu.className = "indexButtonSelected";
        presenceMenu.className = "indexButton";
    }
    if (selectedPage === "Presences") {
        indexMenu.className = "indexButton";
        personMenu.className = "indexButton";
        sectionMenu.className = "indexButton";
        ueMenu.className = "indexButton";
        ueOrgaMenu.className = "indexButton";
        planningMenu.className = "indexButton";
        presenceMenu.className = "indexButtonSelected";
    }
}

package com.careerjinn.search

/**
 * @author David Long
 * Date: 07/08/13
 * Time: 12:34
 */
class PostCodeSubstitution {

    private String postcodePattern = /^\s*([A-z]{1,2})[0-9]{1,2}/;

    private LinkedHashMap map = [
            "AB": "Aberdeen",
            "AL": '"St. Albans" OR "St Albans"',
            "B": "Birmingham",
            "BA": "Bath",
            "BB": "Blackburn",
            "BD": "Bradford",
            "BH": "Bournemouth",
            "BL": "Bolton",
            "BN": "Brighton",
            "BR": "Beckenham",
            "BS": "Bristol",
            "BT": "Belfast",
            "CA": "Carlisle",
            "CB": "Cambridge",
            "CF": "Cardiff",
            "CH": "Chester",
            "CM": "Chelmsford",
            "CO": "Colchester",
            "CR": "Croydon",
            "CT": "Canterbury",
            "CV": "Coventry",
            "CW": "Crewe",
            "DA": "Dartford",
            "DD": "Dundee",
            "DE": "Derby",
            "DG": "Dumfries",
            "DH": "Durham",
            "DL": "Darlington",
            "DN": "Doncaster",
            "DT": "Dorchester",
            "DY": "Dudley",
            "E": "London",
            "EC": "London",
            "EH": "Edinburgh",
            "EN": "Enfield",
            "EX": "Exeter",
            "FK": "Falkirk",
            "FY": "Blackpool",
            "G": "Glasgow",
            "GL": "Gloucester",
            "GU": "Guildford",
            "GY": "Guernsey",
            "HA": "Harrow",
            "HD": "Huddersfield",
            "HG": "Harrogate",
            "HP": "Hemel Hempstead",
            "HR": "Hereford",
            "HS": "Outer Hebrides",
            "HU": "Hull",
            "HX": "Halifax",
            "IG": "Ilford",
            "IM": "Isle of Man",
            "IP": "Ipswich",
            "IV": "Inverness",
            "JE": "Jersey",
            "KA": "Kilmarnock",
            "KT": "Kingston upon Thames",
            "KW": "Kirkwall",
            "KY": "Kirkcaldy",
            "L": "Liverpool",
            "LA": "Lancaster",
            "LD": "Llandrindod Wells",
            "LE": "Leicester",
            "LL": "Llandudno",
            "LN": "Lincoln",
            "LS": "Leeds",
            "LU": "Luton",
            "M,": "anchester",
            "ME": "Maidstone",
            "MK": "Milton Keynes",
            "ML": "Motherwell",
            "N": "London",
            "NE": "Newcastle upon Tyne",
            "NG": "Nottingham",
            "NN": "Northampton",
            "NP": "Newport",
            "NR": "Norwich",
            "NW": "London",
            "OL": "Oldham",
            "OX": "Oxford",
            "PA": "Paisley",
            "PE": "Peterborough",
            "PH": "Perth",
            "PL": "Plymouth OR Cornwall",
            "PO": "Portsmouth",
            "PR": "Preston",
            "RG": "Reading",
            "RH": "Redhill",
            "RM": "Romford",
            "S": "Sheffield",
            "SA": "Swansea",
            "SE": "London",
            "SG": "Stevenage",
            "SK": "Stockport",
            "SL": "Slough",
            "SM": "Sutton",
            "SN": "Swindon",
            "SO": "Southampton",
            "SP": "Salisbury",
            "SR": "Sunderland",
            "SS": "Southend-on-Sea",
            "ST": "Stafford",
            "SW": "London",
            "SY": "Shrewsbury",
            "TA": "Taunton",
            "TD": "Galashiels",
            "TF": "Telford",
            "TN": "Tonbridge",
            "TQ": "Torquay",
            "TR": "Truro OR Cornwall",
            "TS": "Middlesbrough",
            "TW": "Twickenham",
            "UB": "Uxbridge",
            "W": "London",
            "WA": "Warrington",
            "WC": "London",
            "WD": "Watford",
            "WF": "Wakefield",
            "WN": "Wigan",
            "WR": "Worcester",
            "WS": "Walsall",
            "WV": "Wolverhampton",
            "YO": "York",
            "ZE": "Shetland"];

    public String substitutePostCode(String location) {
        if (!(location =~ /^\s*([A-z]{1,2})[0-9]{1,2}/)) {
            return location;
        }
        def matcher = location =~ /^\s*([A-z]{1,2})[0-9]{1,2}/;
        return retrieveSubstituteTown(matcher[0][1]);
    }

    private String retrieveSubstituteTown(String postCodeExtension) {
        if (map[postCodeExtension.toUpperCase()]) {
            return map[postCodeExtension.toUpperCase()];
        }
        return postCodeExtension;
    }

}

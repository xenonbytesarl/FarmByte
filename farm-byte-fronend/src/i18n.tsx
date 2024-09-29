import i18next from "i18next";
import {initReactI18next} from "react-i18next";

import translationEnglish from '@/i18n/en.json';
import translationFrench from '@/i18n/fr.json';


const resources = {
    en: {
        home: translationEnglish
    },
    fr: {
        home: translationFrench
    }
}

i18next
    .use(initReactI18next)
    .init({
      resources,
      lng: "en",
    });

export default i18next;
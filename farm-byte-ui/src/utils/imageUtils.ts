export const getImageUrl = (imageUrl: string): string => {
    return imageUrl? new URL(imageUrl, import.meta.url).href: '';
}

export const fileToBase64 = (string64: string, mime: string): string => {
    return "data:mime;base64,string64".replace('mime', mime).replace('string64', string64);
}

export const pathToFile = async (url: string, mime: string) => {
    const response = await fetch(url);
    const blob = await response.blob();
    const urls = url.split('/');
    const filename = urls.length > 1 ? urls[urls.length - 1]: url;
    return new File([blob], filename, {type: mime})
}


export const fileFromBase64 = (string64: string, fileName:string, mime: string): File =>  {
    const imageContent = atob(string64);
    const buffer = new ArrayBuffer(imageContent.length);
    const view = new Uint8Array(buffer);

    for (let n = 0; n < imageContent.length; n++) {
        view[n] = imageContent.charCodeAt(n);
    }

    const blob = new Blob([buffer], { type: mime });
    return new File([blob], fileName, { lastModified: new Date().getTime(), type: mime });
}

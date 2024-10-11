import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import {useToast} from "@/hooks/use-toast.ts";
import {ProductModel} from "@/pages/inventory/product/ProductModel.ts";
import {useSelector} from "react-redux";
import {store} from "@/Store.ts";
import {
    createProduct,
    findProductById, getCurrentProduct,
    getLoading, resetCurrentProduct,
    updateProduct
} from "@/pages/inventory/product/ProductSlice.ts";
import {ProductCategoryModel} from "@/pages/inventory/product-category/ProductCategoryModel.ts";
import {
    findProductCategories,
    selectProductCategories
} from "@/pages/inventory/product-category/ProductCategorySlice.ts";
import {ChangeEvent, useEffect, useState} from "react";
import {FormModeType} from "@/shared/model/FormModeType.ts";
import {z as Zod} from "zod";
import {ProductTypeEnum} from "@/pages/inventory/product/ProductTypeEnum.ts";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {DEFAULT_DIRECTION_VALUE, MAX_SIZE_VALUE} from "@/constants/page.constant.ts";
import {DEFAULT_PRODUCT_IMAGE, ToastType} from "@/shared/constant/globalConstant.ts";
import {cn} from "@/lib/utils.ts";
import {findUoms, selectUoms} from "@/pages/inventory/uom/UomSlice.ts";
import {UomModel} from "@/pages/inventory/uom/UomModel.ts";
import {Toaster} from "@/components/ui/toaster.tsx";
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import FormCrudButton from "@/components/FormCrudButton.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Label} from "@/components/ui/label.tsx";
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Command, CommandGroup, CommandInput, CommandItem, CommandList} from "@/components/ui/command.tsx";
import {Switch} from "@/components/ui/switch.tsx";
import {unwrapResult} from "@reduxjs/toolkit";
import {fileFromBase64, fileToBase64, getImageUrl, pathToFile} from "@/utils/imageUtils.ts";

const productTypes = [
    {label: 'product_form_type_label_consumable', name: ProductTypeEnum.CONSUMABLE},
    {label: 'product_form_type_label_service', name: ProductTypeEnum.SERVICE},
    {label: 'product_form_type_label_stock', name: ProductTypeEnum.STOCK},
]

const ProductForm = () => {

    const {t} = useTranslation(['home']);

    let {productId} = useParams();

    const {toast} = useToast();

    let product: ProductModel = useSelector(getCurrentProduct);
    const categories: Array<ProductCategoryModel> = useSelector(selectProductCategories);
    const stockUoms: Array<UomModel> = useSelector(selectUoms);
    const purchaseUoms: Array<UomModel> = useSelector(selectUoms);
    const isLoading: boolean = useSelector(getLoading);

    const [openCategoryPopOver, setOpenCategoryPopOver] = useState(false);
    const [categoryPopOverLabel, setCategoryPopOverLabel] = useState("");
    const [openStockUomPopOver, setOpenStockUomPopOver] = useState(false);
    const [stockUomPopOverLabel, setStockUomPopOverLabel] = useState("");
    const [openPurchaseUomPopOver, setOpenPurchaseUomPopOver] = useState(false);
    const [purchaseUomPopOverLabel, setPurchaseUomPopOverLabel] = useState("");
    const [openTypePopOver, setOpenTypePopOver] = useState(false);
    const [typePopOverLabel, setTypePopOverLabel] = useState("");

    const navigate = useNavigate();
    const [mode, setMode] = useState<FormModeType>(productId? FormModeType.READ: FormModeType.CREATE);
    const [fileContent, setFileContent] = useState<File>(null);
    const [filePreview, setFilePreview] = useState('');

    const ProductSchema = Zod.object({
        //reference: Zod.string().trim().min(0).max(16, {message: 'product_form_reference_max_length_message'}),
        name: Zod.string().min(1, {message: t('product_form_name_required_message')}),
        categoryId: Zod.string().min(1, {message: t('product_form_category_id_required_message')}),
        type: Zod.string().min(1, {message: t('product_form_type_required_message')})
    });
    const defaultValuesProduct: ProductModel = {
        id: "",
        name: "",
        reference:"",
        categoryId: "",
        type: "",
        stockUomId: "",
        purchaseUomId: "",
        salePrice: 0,
        purchasePrice: 0,
        sellable: false,
        purchasable: false,
        filename: "",
        encodedFilename: "",
        mime: "",
        active: true
    };
    const { register, handleSubmit, formState: {errors, isValid}, reset, getValues, setValue } = useForm<ProductModel>({
        defaultValues: defaultValuesProduct,
        resolver: zodResolver(ProductSchema),
        mode: "onTouched",
    });

    useEffect(() => {
        store.dispatch(findProductCategories({page: 0, size: MAX_SIZE_VALUE, attribute: "name", direction: DEFAULT_DIRECTION_VALUE}));
        store.dispatch(findUoms({page: 0, size: MAX_SIZE_VALUE, attribute: "name", direction: DEFAULT_DIRECTION_VALUE}));
    }, []);

    useEffect(() => {
        if((!product && productId) || (product && !product.encodedFilename && productId)) {
            store.dispatch(findProductById(productId));
        }
    }, [store.dispatch]);

    useEffect(() => {
        if(product) {
            reset(product)
        }
    }, [product, reset]);

    useEffect(() => {
        if(categoryPopOverLabel) {
            setValue(
                'categoryId',
                categories.find(productCategory => productCategory.name === categoryPopOverLabel).id,
                {shouldTouch: true, shouldDirty: true, shouldValidate: true}
            );
        } else {
            setValue(
                'categoryId',
                '',
                {shouldTouch: true, shouldDirty: true, shouldValidate: false}
            );
        }
    }, [categoryPopOverLabel]);

    useEffect(() => {
        if(stockUomPopOverLabel) {
            setValue(
                'stockUomId',
                stockUoms.find(stockUom => stockUom.name === stockUomPopOverLabel).id,
                {shouldTouch: true, shouldDirty: true, shouldValidate: true}
            );
        } else {
            setValue(
                'stockUomId',
                '',
                {shouldTouch: true, shouldDirty: true, shouldValidate: false}
            );
        }
    }, [stockUomPopOverLabel]);

    useEffect(() => {
        if(purchaseUomPopOverLabel) {
            setValue(
                'purchaseUomId',
                purchaseUoms.find(purchaseUom => purchaseUom.name === purchaseUomPopOverLabel).id,
                {shouldTouch: true, shouldDirty: true, shouldValidate: true}
            );
        } else {
            setValue(
                'purchaseUomId',
                '',
                {shouldTouch: true, shouldDirty: true, shouldValidate: false}
                );
        }
    }, [purchaseUomPopOverLabel]);

    useEffect(() => {
        if(typePopOverLabel) {
            setValue(
                'type',
                productTypes.find(productType => productType.label === typePopOverLabel).name,
                {shouldTouch: true, shouldDirty: true, shouldValidate: true}
            );
        }else {
            setValue('type', "",{shouldTouch: true, shouldDirty: true, shouldValidate: false});
        }
    }, [typePopOverLabel]);

    const showToast = (variant: ToastType, message: string) => {
        toast({
            className: cn(
                'top-0 right-0 flex fixed md:max-w-[420px] md:top-4 md:right-4'
            ),
            variant: variant,
            title: "FarmByte",
            description: t(message),
        });
    }

    const onSubmit = () => {
        const productFormValue: ProductModel = getValues();
        if (productFormValue.id) {
            const fileValues: File = fileContent === null || fileContent === undefined
                ?
                fileFromBase64(productFormValue.encodedFilename, productFormValue.filename, productFormValue.mime)
                : fileContent;
            store.dispatch(updateProduct({productId: productFormValue.id, product: productFormValue, file: fileValues}))
                .then(unwrapResult)
                .then((response) => {
                    setMode(FormModeType.READ);
                    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                    // @ts-expect-error
                    showToast("info", response.message);
                })
                .catch((error) => {
                    setMode(FormModeType.EDIT);
                    showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                })
        } else {
            const fileValues: File = fileContent;
            productFormValue.active=true
            productFormValue.sellable=true
            productFormValue.purchasable=true
            if(fileValues === null || fileValues === undefined) {
                pathToFile(DEFAULT_PRODUCT_IMAGE, 'image/png').then((response) => {
                    store.dispatch(createProduct({product: productFormValue, file: response}))
                        .then(unwrapResult)
                        .then((response) => {
                            setMode(FormModeType.READ);
                            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                            // @ts-expect-error
                            showToast("success", response.message);
                            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                            // @ts-expect-error
                            navigate(`/inventory/products/details/${response.data.content.id}`);
                        })
                        .catch((error) => {
                            setMode(FormModeType.CREATE);
                            showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                        });
                })
            } else {
                store.dispatch(createProduct({product: productFormValue, file: fileValues}))
                    .then(unwrapResult)
                    .then((response) => {
                        setMode(FormModeType.READ);
                        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                        // @ts-expect-error
                        showToast("success", response.message);
                        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                        // @ts-expect-error
                        navigate(`/inventory/products/details/${response.data.content.id}`);
                    })
                    .catch((error) => {
                        setMode(FormModeType.CREATE);
                        showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                    });
            }

        }
    }

    const onEdit = () => {
        setMode(FormModeType.EDIT);
    }

    const onCancel = () => {
        if(product) {
            reset(product);
            setMode(FormModeType.READ);
        } else {
            reset();
        }
    }

    const onCreate = () => {
        setMode(FormModeType.CREATE);
        reset(defaultValuesProduct);
        resetPopOverLabel();
        store.dispatch(resetCurrentProduct())
        navigate('/inventory/products/new');
    }

    const resetPopOverLabel = () =>{
        setCategoryPopOverLabel('');
        setTypePopOverLabel('');
        setPurchaseUomPopOverLabel('');
        setStockUomPopOverLabel('');
        setOpenCategoryPopOver(false);
        setOpenTypePopOver(false);
        setOpenPurchaseUomPopOver(false);
        setOpenStockUomPopOver(false);
    }


    const handleFileChange = (event: ChangeEvent<HTMLInputElement>) => {
        const content = event.target.files[0];
        if(content) {
            setFilePreview(URL.createObjectURL(content));
            setFileContent(content);
        }
    }
    return (
        <form onSubmit={handleSubmit(onSubmit)} noValidate>
            <Toaster/>
            <Card className="">
                <CardHeader>
                    <CardTitle className="flex flex-row justify-start items-center text-primary gap-5">
                        <span onClick={() => navigate(`/inventory/products`)}
                              className="material-symbols-outlined text-3xl cursor-pointer">arrow_back</span>
                        <span
                            className="text-2xl">{t(productId ? 'product_form_edit_title' : 'product_form_new_title')}</span>
                    </CardTitle>
                    <CardDescription>
                    <span className="flex flex-row w-full m-5">
                        <FormCrudButton
                            mode={mode}
                            isLoading={isLoading}
                            isValid={isValid}
                            onEdit={onEdit}
                            onCancel={onCancel}
                            onCreate={onCreate}
                        />
                        <span className="flex flex-row justify-end items-center gap-3 w-6/12">
                        </span>
                    </span>
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <Input id="name" type="hidden" {...register("id")} />
                    <div className="flex flex-col justify-center items-center gap-5">
                        <div className="flex flex-col lg:flex-row justify-start items-center w-full">
                            <div className="relative flex flex-col justify-center items-center w-72">
                                <img alt="..." className={`size-48 bg-white object-cover
                                    flex flex-col items-center justify-center rounded-lg shadow-gray-200 shadow-lg
                                    gap-4 transition-all duration-1000 hover:text-primary`}
                                     src={filePreview ? filePreview : product? fileToBase64(product?.encodedFilename, product?.mime): getImageUrl(DEFAULT_PRODUCT_IMAGE)}/>
                                <input className={`absolute top-0 size-48 opacity-0 ${mode === FormModeType.READ || isLoading? 'cursor-auto': 'cursor-pointer'}`}
                                       accept="image/x-png, image/jpeg" type="file" disabled={mode === FormModeType.READ || isLoading}
                                       onChange={handleFileChange}/>

                            </div>
                            <div className="flex flex-col lg:flex-row w-full gap-4">
                                <div className="lg:w-6/12 mb-4">
                                    <div className="flex flex-col space-y-1.5 mb-5">
                                        <Label htmlFor="reference">{t('product_form_reference_label')}</Label>
                                        <Input id="reference" type="text" {...register("reference")}
                                               disabled={mode === FormModeType.READ || isLoading}/>
                                        <small className="text-red-500">{errors.reference?.message}</small>
                                    </div>
                                    <div className="flex flex-col space-y-1.5 mb-5">
                                        <Label htmlFor="uomType">{t('product_form_type_label')}</Label>
                                        <Popover open={openTypePopOver} onOpenChange={setOpenTypePopOver}>
                                            <PopoverTrigger asChild>
                                                <Button
                                                    variant="outline"
                                                    role="combobox"
                                                    aria-expanded={openTypePopOver}
                                                    className="justify-between"
                                                    disabled={mode === FormModeType.READ || isLoading}
                                                >
                                                    <span>{typePopOverLabel
                                                        ? t(productTypes.find((type) => type.label === typePopOverLabel)?.label)
                                                        : product ? t(productTypes.find((typeEdit) => typeEdit.name === product.type)?.label) : t('product_form_type_pop_over_place_holder')}</span>
                                                    <span className="opacity-50 material-symbols-outlined">unfold_more</span>
                                                </Button>
                                            </PopoverTrigger>
                                            <PopoverContent className="w-[--radix-popover-trigger-width]">
                                                <Command>
                                                    <CommandInput id="uomType" {...register("type")}/>
                                                    <CommandList>
                                                        <Command>{t('product_form_pop_type_over_not_found')}</Command>
                                                        <CommandGroup>
                                                            {productTypes.map((type) => (
                                                                <CommandItem
                                                                    key={type.label}
                                                                    value={type.label}
                                                                    onSelect={(currentValue) => {
                                                                        setTypePopOverLabel(currentValue === typePopOverLabel ? "" : currentValue)
                                                                        setOpenTypePopOver(false)
                                                                    }}
                                                                >
                                                        <span
                                                            className={`mr-2 h-4 w-4 material-symbols-outlined ${typePopOverLabel === type.label ? 'opacity-100' : 'opacity-0'}`}
                                                        >check</span>
                                                                    {t(type.label)}
                                                                </CommandItem>
                                                            ))}
                                                        </CommandGroup>
                                                    </CommandList>
                                                </Command>
                                            </PopoverContent>
                                        </Popover>
                                        <small className="text-red-500">{errors.type?.message}</small>
                                    </div>
                                </div>
                                <div className="lg:w-6/12 mb-4">
                                    <div className="flex flex-col space-y-1.5 mb-5">
                                        <Label htmlFor="name">{t('product_form_name_label')}</Label>
                                        <Input id="name" type="text" {...register("name")}
                                               disabled={mode === FormModeType.READ || isLoading}/>
                                        <small className="text-red-500">{errors.name?.message}</small>
                                    </div>
                                    <div className="flex flex-col space-y-1.5 mb-5">
                                        <Label htmlFor="categoryId">{t('product_form_category_id_label')}</Label>
                                        <Popover open={openCategoryPopOver} onOpenChange={setOpenCategoryPopOver}>
                                            <PopoverTrigger asChild>
                                                <Button
                                                    variant="outline"
                                                    role="combobox"
                                                    aria-expanded={openCategoryPopOver}
                                                    className="justify-between"
                                                    disabled={mode === FormModeType.READ || isLoading}
                                                >
                                                    <span>{categoryPopOverLabel
                                                        ? categories.find((category) => category.name === categoryPopOverLabel)?.name
                                                        : product ? categories.find((category) => category.id === product.categoryId)?.name : t('product_form_category_pop_over_place_holder')}</span>
                                                    <span
                                                        className="opacity-50 material-symbols-outlined">unfold_more</span>
                                                </Button>
                                            </PopoverTrigger>
                                            <PopoverContent className="w-[--radix-popover-trigger-width]">
                                                <Command>
                                                    <CommandInput id="categoryId" {...register("categoryId")}/>
                                                    <CommandList>
                                                        <Command>{t('product_form_category_pop_over_not_found')}</Command>
                                                        <CommandGroup>
                                                            {categories.map((category) => (
                                                                <CommandItem
                                                                    key={category.id}
                                                                    value={category.name}
                                                                    onSelect={(currentValue) => {
                                                                        setCategoryPopOverLabel(currentValue === categoryPopOverLabel ? "" : currentValue)
                                                                        setOpenCategoryPopOver(false)
                                                                    }}
                                                                >
                                                        <span
                                                            className={`mr-2 h-4 w-4 material-symbols-outlined ${categoryPopOverLabel === category.name ? 'opacity-100' : 'opacity-0'}`}
                                                        >check</span>
                                                                    {category.name}
                                                                </CommandItem>
                                                            ))}
                                                        </CommandGroup>
                                                    </CommandList>
                                                </Command>
                                            </PopoverContent>
                                        </Popover>
                                        <small className="text-red-500">{errors.categoryId?.message}</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="flex flex-col lg:flex-row w-full gap-4 mt-4">
                            <div className="lg:w-6/12 mb-4">
                                <div className="flex flex-col space-y-1.5 mb-5">
                                    <Label htmlFor="stockUomId">{t('product_form_stock_uom_id_label')}</Label>
                                    <Popover open={openStockUomPopOver} onOpenChange={setOpenStockUomPopOver}>
                                        <PopoverTrigger asChild>
                                            <Button
                                                variant="outline"
                                                role="combobox"
                                                aria-expanded={openStockUomPopOver}
                                                className="justify-between"
                                                disabled={mode === FormModeType.READ || isLoading}
                                            >
                                                <span>{stockUomPopOverLabel
                                                    ? stockUoms.find((stockUom) => stockUom.name === stockUomPopOverLabel)?.name
                                                    : product ? stockUoms.find((stockUom) => stockUom.id === product.stockUomId)?.name : t('product_form_stock_uom_id_pop_over_place_holder')}</span>
                                                <span
                                                    className="opacity-50 material-symbols-outlined">unfold_more</span>
                                            </Button>
                                        </PopoverTrigger>
                                        <PopoverContent className="w-[--radix-popover-trigger-width]">
                                            <Command>
                                                <CommandInput id="stockUomId" {...register("stockUomId")}/>
                                                <CommandList>
                                                    <Command>{t('product_form_stock_uom_id_pop_over_not_found')}</Command>
                                                    <CommandGroup>
                                                        {stockUoms.map((stockUom) => (
                                                            <CommandItem
                                                                key={stockUom.id}
                                                                value={stockUom.name}
                                                                onSelect={(currentValue) => {
                                                                    setStockUomPopOverLabel(currentValue === stockUomPopOverLabel ? "" : currentValue)
                                                                    setOpenStockUomPopOver(false)
                                                                }}
                                                            >
                                                        <span
                                                            className={`mr-2 h-4 w-4 material-symbols-outlined ${stockUomPopOverLabel === stockUom.name ? 'opacity-100' : 'opacity-0'}`}
                                                        >check</span>
                                                                {stockUom.name}
                                                            </CommandItem>
                                                        ))}
                                                    </CommandGroup>
                                                </CommandList>
                                            </Command>
                                        </PopoverContent>
                                    </Popover>
                                </div>
                                <div className="flex flex-col space-y-1.5 mb-5">
                                    <Label htmlFor="categoryId">{t('product_form_purchase_uom_id_label')}</Label>
                                    <Popover open={openPurchaseUomPopOver} onOpenChange={setOpenPurchaseUomPopOver}>
                                        <PopoverTrigger asChild>
                                            <Button
                                                variant="outline"
                                                role="combobox"
                                                aria-expanded={openPurchaseUomPopOver}
                                                className="justify-between"
                                                disabled={mode === FormModeType.READ || isLoading}
                                            >
                                                <span>{purchaseUomPopOverLabel
                                                    ? purchaseUoms.find((purchaseUom) => purchaseUom.name === purchaseUomPopOverLabel)?.name
                                                    : product ? purchaseUoms.find((purchaseUom) => purchaseUom.id === product.purchaseUomId)?.name : t('product_form_purchase_uom_id_pop_over_place_holder')}</span>
                                                <span
                                                    className="opacity-50 material-symbols-outlined">unfold_more</span>
                                            </Button>
                                        </PopoverTrigger>
                                        <PopoverContent className="w-[--radix-popover-trigger-width]">
                                            <Command>
                                                <CommandInput id="purchaseUomId" {...register("purchaseUomId")}/>
                                                <CommandList>
                                                    <Command>{t('product_form_purchase_uom_id_pop_over_not_found')}</Command>
                                                    <CommandGroup>
                                                        {purchaseUoms.map((purchaseUom) => (
                                                            <CommandItem
                                                                key={purchaseUom.id}
                                                                value={purchaseUom.name}
                                                                onSelect={(currentValue) => {
                                                                    setPurchaseUomPopOverLabel(currentValue === purchaseUomPopOverLabel ? "" : currentValue)
                                                                    setOpenPurchaseUomPopOver(false)
                                                                }}
                                                            >
                                                        <span
                                                            className={`mr-2 h-4 w-4 material-symbols-outlined ${purchaseUomPopOverLabel === purchaseUom.name ? 'opacity-100' : 'opacity-0'}`}
                                                        >check</span>
                                                                {purchaseUom.name}
                                                            </CommandItem>
                                                        ))}
                                                    </CommandGroup>
                                                </CommandList>
                                            </Command>
                                        </PopoverContent>
                                    </Popover>
                                </div>
                            </div>
                            <div className="lg:w-6/12 mb-4">
                                <div className="flex flex-col space-y-1.5 mb-5">
                                    <Label htmlFor="purchasePrice">{t('product_form_purchase_price_label')}</Label>
                                    <Input id="purchasePrice" type="number" {...register("purchasePrice")}
                                           disabled={mode === FormModeType.READ || isLoading}/>
                                </div>
                                <div className="flex flex-col space-y-1.5 mb-5">
                                    <Label htmlFor="salePrice">{t('product_form_sale_price_label')}</Label>
                                    <Input id="salePrice" type="number" {...register("salePrice")}
                                           disabled={mode === FormModeType.READ || isLoading}/>
                                </div>
                            </div>
                        </div>
                        <div className="flex flex-row gap-4 mt-4 w-full">
                            <div className="flex flex-row justify-start items-center space-x-1.5 w-4/12 gap-4">
                                <Switch id="purchasable" {...register("purchasable")}/>
                                <Label htmlFor="purchasable">{t('product_form_purchasable_label')}</Label>
                            </div>
                            <div className="flex flex-row justify-start items-center space-x-1.5 w-4/12 gap-4">
                                <Switch id="sellable" {...register("sellable")}/>
                                <Label htmlFor="sellable">{t('product_form_sellable_label')}</Label>
                            </div>
                            <div className="flex flex-row justify-start items-center space-y-1.5 w-4/12 gap-4">
                                <Switch id="active" {...register("active")}/>
                                <Label htmlFor="active">{t('product_form_active_label')}</Label>
                            </div>
                        </div>
                    </div>
                </CardContent>
                <CardFooter className="flex justify-between py-5">

                </CardFooter>
            </Card>
        </form>
    );
};

export default ProductForm;
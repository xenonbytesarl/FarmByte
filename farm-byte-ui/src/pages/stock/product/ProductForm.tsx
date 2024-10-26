import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import {useToast} from "@/hooks/use-toast.ts";
import {ProductModel} from "@/pages/stock/product/ProductModel.ts";
import {useSelector, useDispatch} from "react-redux";
import {RootDispatch} from "@/Store.ts";
import {
    createProduct,
    findProductById, getCurrentProduct,
    getLoading, resetCurrentProduct,
    updateProduct
} from "@/pages/stock/product/ProductSlice.ts";
import {ProductCategoryModel} from "@/pages/stock/product-category/ProductCategoryModel.ts";
import {
    findProductCategories,
    selectProductCategories
} from "@/pages/stock/product-category/ProductCategorySlice.ts";
import {ChangeEvent, useEffect, useState} from "react";
import {FormModeType} from "@/shared/model/FormModeType.ts";
import {z as Zod} from "zod";
import {ProductTypeEnum} from "@/pages/stock/product/ProductTypeEnum.ts";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {DEFAULT_DIRECTION_VALUE, MAX_SIZE_VALUE} from "@/constants/page.constant.ts";
import {DEFAULT_PRODUCT_IMAGE, ToastType} from "@/shared/constant/globalConstant.ts";
import {cn} from "@/lib/utils.ts";
import {findUoms, selectUoms} from "@/pages/stock/uom/UomSlice.ts";
import {UomModel} from "@/pages/stock/uom/UomModel.ts";
import {Toaster} from "@/components/ui/toaster.tsx";
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import FormCrudButton from "@/components/FormCrudButton.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Command, CommandGroup, CommandInput, CommandItem, CommandList} from "@/components/ui/command.tsx";
import {unwrapResult} from "@reduxjs/toolkit";
import {fileFromBase64, fileToBase64, getImageUrl, pathToFile} from "@/utils/imageUtils.ts";
import {changeNullToEmptyString} from "@/utils/changeNullToEmptyString.ts";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Checkbox} from "@/components/ui/checkbox.tsx";

const productTypes = [
    {label: 'product_form_type_label_consumable', name: ProductTypeEnum.CONSUMABLE},
    {label: 'product_form_type_label_service', name: ProductTypeEnum.SERVICE},
    {label: 'product_form_type_label_stock', name: ProductTypeEnum.STOCK},
]

const ProductForm = () => {

    const {t} = useTranslation(['home']);

    const {productId} = useParams();

    const {toast} = useToast();

    const product = useSelector(getCurrentProduct);
    const categories: Array<ProductCategoryModel> = useSelector(selectProductCategories);
    const stockUoms: Array<UomModel> = useSelector(selectUoms);
    const purchaseUoms: Array<UomModel> = useSelector(selectUoms);
    const isLoading: boolean = useSelector(getLoading);
    const dispatch = useDispatch<RootDispatch>();

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
    // @ts-ignore
    const [fileContent, setFileContent] = useState<File>(null);
    const [filePreview, setFilePreview] = useState('');

    const isUomInSameCategory = (purchaseUomId: string, stockUomId: string): boolean => {
        if(stockUomId !== '' && purchaseUomId !== '') {
            const stockUom = stockUoms.find((stockUom) => stockUom.id === stockUomId);
            const purchaseUom = purchaseUoms.find((purchaseUom) => purchaseUom.id === purchaseUomId);
            return stockUom && purchaseUom? stockUom.uomCategoryId == purchaseUom.uomCategoryId: false;
        }
        return false;

    }

    const ProductSchema = Zod.object({
        id: Zod.string().min(0),
        reference: Zod.string().min(0).and(Zod.string().max(16, {message: 'product_form_reference_max_length_message'})),
        name: Zod.string().min(1, {message: t('product_form_name_required_message')}),
        categoryId: Zod.string().min(1, {message: t('product_form_category_id_required_message')}),
        type: Zod.string().min(1, {message: t('product_form_type_required_message')}),
        stockUomId: Zod.string().min(0),
        purchaseUomId: Zod.string().min(0),
        purchasePrice: Zod.coerce.number().nonnegative({message: t('product_form_purchase_price_positive_message')}),
        salePrice: Zod.coerce.number().nonnegative({message: t('product_form_sale_price_positive_message')}),
        purchasable: Zod.boolean(),
        sellable: Zod.boolean(),
        filename: Zod.string().min(0),
        encodedFile: Zod.string().min(0),
        mime: Zod.string().min(0),
        active: Zod.boolean(),
    }).superRefine((args, ctx) => {
        if(args.stockUomId !== '' && args.purchaseUomId !== '') {
            if(!isUomInSameCategory(args.purchaseUomId, args.stockUomId)) {
                ctx.addIssue({
                    code: Zod.ZodIssueCode.custom,
                    fatal: true,
                    path: ['stockUomId'],
                    message: t('product_form_purchase_and_stock_uom_invalid_category_message'),
                });
                ctx.addIssue({
                    code: Zod.ZodIssueCode.custom,
                    fatal: true,
                    path: ['purchaseUomId'],
                    message: t('product_form_purchase_and_stock_uom_invalid_category_message'),
                })
            }
        }
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
        encodedFile: "",
        mime: "",
        active: true
    };
    const form = useForm<Zod.infer<typeof ProductSchema>>({
        defaultValues: defaultValuesProduct,
        resolver: zodResolver(ProductSchema),
        mode: "onChange",
    });

    useEffect(() => {
        dispatch(findProductCategories({page: 0, size: MAX_SIZE_VALUE, attribute: "name", direction: DEFAULT_DIRECTION_VALUE}));
        dispatch(findUoms({page: 0, size: MAX_SIZE_VALUE, attribute: "name", direction: DEFAULT_DIRECTION_VALUE}));
    }, []);

    useEffect(() => {
        // @ts-ignore
        if((!product && productId) || (product && !product.encodedFilename && productId)) {
            dispatch(findProductById(productId));
        }
    }, [dispatch]);

    useEffect(() => {
        if(product) {
            form.reset(changeNullToEmptyString(product))
        }
    }, [product]);


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
        const productFormValue: ProductModel = form.getValues() as ProductModel;
        if (productFormValue.id) {
            const fileValues: File = fileContent === null || fileContent === undefined
                ?
                fileFromBase64(productFormValue.encodedFile, productFormValue.filename, productFormValue.mime)
                : fileContent;
            dispatch(updateProduct({productId: productFormValue.id, product: productFormValue, file: fileValues}))
                .then(unwrapResult)
                .then((response) => {
                    setMode(FormModeType.READ);
                    showToast("info", response.message);
                })
                .catch((error) => {
                    setMode(FormModeType.EDIT);
                    showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                })
        } else {
            const fileValues: File = fileContent;
            if(fileValues === null || fileValues === undefined) {
                pathToFile(DEFAULT_PRODUCT_IMAGE, 'image/png').then((response) => {
                    dispatch(createProduct({product: productFormValue, file: response}))
                        .then(unwrapResult)
                        .then((response) => {
                            setMode(FormModeType.READ);
                            showToast("success", response.message);
                            navigate(`/stock/products/details/${response.content.id}`);
                        })
                        .catch((error) => {
                            setMode(FormModeType.CREATE);
                            showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                        });
                })
            } else {
                dispatch(createProduct({product: productFormValue, file: fileValues}))
                    .then(unwrapResult)
                    .then((response) => {
                        setMode(FormModeType.READ);
                        showToast("success", response.message);
                        navigate(`/stock/products/details/${response.content.id}`);
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
            form.reset(changeNullToEmptyString(product));
            setMode(FormModeType.READ);
            resetPopOverLabel(product);
        } else {
            form.reset();
            resetPopOverLabel(undefined);
        }
    }

    const onCreate = () => {
        setMode(FormModeType.CREATE);
        form.reset(defaultValuesProduct);
        resetPopOverLabel(undefined);
        dispatch(resetCurrentProduct())
        navigate('/stock/products/new');
    }

    const resetPopOverLabel = (product: ProductModel | undefined) =>{
        if(product) {
            setCategoryPopOverLabel(categories.find(category => category.id === product.categoryId)?.name as string);
            setPurchaseUomPopOverLabel(product.purchaseUomId? purchaseUoms.find(purchaseUom => purchaseUom.id === product.purchaseUomId)?.name as string: '');
            setStockUomPopOverLabel(product.stockUomId? stockUoms.find(stockUom => stockUom.id === product.stockUomId)?.name as string: '');
            setTypePopOverLabel(productTypes.find(type => product.type === type.name)?.label as string);
        } else {
            setCategoryPopOverLabel('');
            setTypePopOverLabel('');
            setPurchaseUomPopOverLabel('');
            setStockUomPopOverLabel('');
        }
        setOpenCategoryPopOver(false);
        setOpenTypePopOver(false);
        setOpenPurchaseUomPopOver(false);
        setOpenStockUomPopOver(false);
    }


    const handleFileChange = (event: ChangeEvent<HTMLInputElement>) => {
        const content = event.target && event.target.files && event.target.files[0] || null;
        if(content) {
            setFilePreview(URL.createObjectURL(content));
            setFileContent(content);
        }
    }
    return (
        <div className="p-10">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} noValidate>
                    <Toaster/>
                    <Card className="">
                        <CardHeader>
                            <CardTitle className="flex flex-row justify-start items-center text-primary gap-5">
                        <span onClick={() => navigate(`/stock/products`)}
                              className="material-symbols-outlined text-3xl cursor-pointer">arrow_back</span>
                                <span
                                    className="text-2xl">{t(productId ? 'product_form_edit_title' : 'product_form_new_title')}</span>
                            </CardTitle>
                            <CardDescription>
                    <span className="flex flex-row w-full m-5">
                        <FormCrudButton
                            mode={mode}
                            isLoading={isLoading}
                            isValid={form.formState.isValid}
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
                            <FormField
                                control={form.control}
                                name="id"
                                render={({field}) => (
                                    <FormItem>
                                        <FormControl>
                                            <Input id="id" type="hidden" {...field} />
                                        </FormControl>
                                    </FormItem>
                                )}
                            />
                            <div className="flex flex-col justify-center items-center gap-5">
                                <div className="flex flex-col lg:flex-row justify-start items-center w-full">
                                    <div className="relative flex flex-col justify-center items-center w-72">
                                        <img alt="..." className={`size-48 bg-white object-cover
                                    flex flex-col items-center justify-center rounded-lg shadow-gray-200 shadow-lg
                                    gap-4 transition-all duration-1000 hover:text-primary`}
                                            //@ts-ignore
                                             src={filePreview ? filePreview : product ? fileToBase64(product?.encodedFile, product?.mime) : getImageUrl(DEFAULT_PRODUCT_IMAGE)}/>
                                        <input
                                            className={`absolute top-0 size-48 opacity-0 ${mode === FormModeType.READ || isLoading ? 'cursor-auto' : 'cursor-pointer'}`}
                                            accept="image/x-png, image/jpeg" type="file"
                                            disabled={mode === FormModeType.READ || isLoading}
                                            onChange={handleFileChange}/>

                                    </div>
                                    <div className="flex flex-col lg:flex-row w-full gap-4">
                                        <div className="lg:w-6/12 mb-4">
                                            <div className="flex flex-col space-y-1.5 mb-5">
                                                <FormField
                                                    control={form.control}
                                                    name="reference"
                                                    render={({field}) => (
                                                        <FormItem>
                                                            <FormLabel>{t('product_form_reference_label')}</FormLabel>
                                                            <FormControl>
                                                                <Input id="reference" type="text" {...field}
                                                                       disabled={mode === FormModeType.READ || isLoading}/>
                                                            </FormControl>
                                                            <FormMessage className="text-xs text-destructive"/>
                                                        </FormItem>
                                                    )}
                                                />
                                            </div>
                                            <div className="flex flex-col space-y-1.5 mb-5">
                                                <FormField
                                                    control={form.control}
                                                    name="type"
                                                    render={() => (
                                                        <FormItem>
                                                            <FormLabel>{t('product_form_type_label')}</FormLabel>
                                                            <FormControl>
                                                                <Popover open={openTypePopOver} onOpenChange={setOpenTypePopOver}>
                                                                    <PopoverTrigger asChild>
                                                                        <Button
                                                                            variant="outline"
                                                                            role="combobox"
                                                                            aria-expanded={openTypePopOver}
                                                                            className="w-full justify-between"
                                                                            disabled={mode === FormModeType.READ || isLoading}
                                                                        >
                                                                        <span>{typePopOverLabel
                                                                            //@ts-ignore
                                                                            ? t(productTypes.find((type) => type.label === typePopOverLabel)?.label)
                                                                            //@ts-ignore
                                                                            : product ? t(productTypes.find((typeEdit) => typeEdit.name === product.type)?.label) : t('product_form_type_pop_over_place_holder')}</span>
                                                                            <span
                                                                                className="opacity-50 material-symbols-outlined">unfold_more</span>
                                                                        </Button>
                                                                    </PopoverTrigger>
                                                                    <PopoverContent className="w-[--radix-popover-trigger-width]">
                                                                        <Command>
                                                                            <CommandInput id="type" placeholder={t('product_form_type_pop_over_place_holder')} />
                                                                            <CommandList>
                                                                                <Command>{t('product_form_pop_type_over_not_found')}</Command>
                                                                                <CommandGroup>
                                                                                    {productTypes.map((type) => (
                                                                                        <CommandItem
                                                                                            key={type.label}
                                                                                            value={type.label}
                                                                                            onSelect={(currentValue) => {
                                                                                                setTypePopOverLabel(currentValue === typePopOverLabel ? "" : currentValue);
                                                                                                setOpenTypePopOver(false);
                                                                                                form.setValue(
                                                                                                    "type",
                                                                                                    currentValue === typePopOverLabel ? "" : type.name,
                                                                                                    {shouldTouch: true, shouldDirty: true, shouldValidate: true}
                                                                                                );
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
                                                            </FormControl>
                                                            <FormMessage className="text-xs text-destructive"/>
                                                        </FormItem>
                                                    )}
                                                />
                                            </div>
                                        </div>
                                        <div className="lg:w-6/12 mb-4">
                                            <div className="flex flex-col space-y-1.5 mb-5">
                                                <FormField
                                                    control={form.control}
                                                    name="name"
                                                    render={({field}) => (
                                                        <FormItem>
                                                            <FormLabel>{t('product_form_name_label')}</FormLabel>
                                                            <FormControl>
                                                                <Input id="name" type="text" {...field}
                                                                       disabled={mode === FormModeType.READ || isLoading}/>
                                                            </FormControl>
                                                            <FormMessage className="text-xs text-destructive"/>
                                                        </FormItem>
                                                    )}
                                                />
                                            </div>
                                            <div className="flex flex-col space-y-1.5 mb-5">
                                                <FormField
                                                    control={form.control}
                                                    name="categoryId"
                                                    render={() => (
                                                        <FormItem>
                                                            <FormLabel>{t('product_form_category_id_label')}</FormLabel>
                                                            <FormControl>
                                                                <Popover open={openCategoryPopOver} onOpenChange={setOpenCategoryPopOver}>
                                                                    <PopoverTrigger asChild>
                                                                        <Button
                                                                            variant="outline"
                                                                            role="combobox"
                                                                            aria-expanded={openCategoryPopOver}
                                                                            className="w-full justify-between"
                                                                            disabled={mode === FormModeType.READ || isLoading}
                                                                        >
                                                                        <span>{categoryPopOverLabel
                                                                            ? categories.find((category) => category.name === categoryPopOverLabel)?.name
                                                                            //@ts-ignore
                                                                            : product ? categories.find((category) => category.id === product.categoryId)?.name : t('product_form_category_pop_over_place_holder')}</span>
                                                                            <span
                                                                                className="opacity-50 material-symbols-outlined">unfold_more</span>
                                                                        </Button>
                                                                    </PopoverTrigger>
                                                                    <PopoverContent className="w-[--radix-popover-trigger-width]">
                                                                        <Command>
                                                                            <CommandInput id="categoryId" placeholder={t('product_form_category_pop_over_place_holder')} />
                                                                            <CommandList>
                                                                                <Command>{t('product_form_category_pop_over_not_found')}</Command>
                                                                                <CommandGroup>
                                                                                    {categories.map((category) => (
                                                                                        <CommandItem
                                                                                            key={category.id}
                                                                                            value={category.name}
                                                                                            onSelect={(currentValue) => {
                                                                                                setCategoryPopOverLabel(currentValue === categoryPopOverLabel ? "" : currentValue);
                                                                                                setOpenCategoryPopOver(false);
                                                                                                form.setValue(
                                                                                                    "categoryId",
                                                                                                    currentValue === categoryPopOverLabel ? "" : category.id,
                                                                                                    {shouldTouch: true, shouldDirty: true, shouldValidate: true}
                                                                                                );
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
                                                            </FormControl>
                                                            <FormMessage className="text-xs text-destructive"/>
                                                        </FormItem>
                                                    )}
                                                />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="flex flex-col lg:flex-row w-full gap-4 mt-4">
                                    <div className="lg:w-6/12 mb-4">
                                        <div className="flex flex-col space-y-1.5 mb-5">
                                            <FormField
                                                control={form.control}
                                                name="stockUomId"
                                                render={() => (
                                                    <FormItem>
                                                        <FormLabel>{t('product_form_stock_uom_id_label')}</FormLabel>
                                                        <FormControl>
                                                            <Popover open={openStockUomPopOver} onOpenChange={setOpenStockUomPopOver}>
                                                                <PopoverTrigger asChild>
                                                                    <Button
                                                                        variant="outline"
                                                                        role="combobox"
                                                                        aria-expanded={openStockUomPopOver}
                                                                        className="w-full justify-between"
                                                                        disabled={mode === FormModeType.READ || isLoading  || form.getValues('type') !== ProductTypeEnum.STOCK.valueOf()}
                                                                    >
                                                                    <span>{stockUomPopOverLabel
                                                                        ? stockUoms.find((stockUom) => stockUom.name === stockUomPopOverLabel)?.name
                                                                        //@ts-ignore
                                                                        : product ? stockUoms.find((stockUom) => stockUom.id === product.stockUomId)?.name : t('product_form_stock_uom_id_pop_over_place_holder')}</span>
                                                                        <span
                                                                            className="opacity-50 material-symbols-outlined">unfold_more</span>
                                                                    </Button>
                                                                </PopoverTrigger>
                                                                <PopoverContent className="w-[--radix-popover-trigger-width]">
                                                                    <Command>
                                                                        <CommandInput id="stockUomId" placeholder={t('product_form_stock_uom_id_pop_over_place_holder')}/>
                                                                        <CommandList>
                                                                            <Command>{t('product_form_stock_uom_id_pop_over_not_found')}</Command>
                                                                            <CommandGroup>
                                                                                {stockUoms.map((stockUom) => (
                                                                                    <CommandItem
                                                                                        key={stockUom.id}
                                                                                        value={stockUom.name}
                                                                                        onSelect={(currentValue) => {
                                                                                            setStockUomPopOverLabel(currentValue === stockUomPopOverLabel ? "" : currentValue);
                                                                                            setOpenStockUomPopOver(false);
                                                                                            form.setValue(
                                                                                                "stockUomId",
                                                                                                currentValue === stockUomPopOverLabel ? "" : stockUom.id,
                                                                                                {shouldTouch: true, shouldDirty: true, shouldValidate: currentValue !== stockUomPopOverLabel}
                                                                                            );
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
                                                        </FormControl>
                                                        <FormMessage className="text-xs text-destructive"/>
                                                    </FormItem>
                                                )}
                                            />
                                        </div>
                                        <div className="flex flex-col space-y-1.5 mb-5">
                                            <FormField
                                                control={form.control}
                                                name="purchaseUomId"
                                                render={() => (
                                                    <FormItem>
                                                        <FormLabel>{t('product_form_purchase_uom_id_label')}</FormLabel>
                                                        <FormControl>
                                                            <Popover open={openPurchaseUomPopOver} onOpenChange={setOpenPurchaseUomPopOver}>
                                                                <PopoverTrigger asChild>
                                                                    <Button
                                                                        variant="outline"
                                                                        role="combobox"
                                                                        aria-expanded={openPurchaseUomPopOver}
                                                                        className="w-full justify-between"
                                                                        disabled={mode === FormModeType.READ || isLoading  || form.getValues('type') !== ProductTypeEnum.STOCK.valueOf()}
                                                                    >
                                                                    <span>{purchaseUomPopOverLabel
                                                                        ? purchaseUoms.find((purchaseUom) => purchaseUom.name === purchaseUomPopOverLabel)?.name
                                                                        //@ts-ignore
                                                                        : product ? purchaseUoms.find((purchaseUom) => purchaseUom.id === product.purchaseUomId)?.name : t('product_form_purchase_uom_id_pop_over_place_holder')}</span>
                                                                        <span
                                                                            className="opacity-50 material-symbols-outlined">unfold_more</span>
                                                                    </Button>
                                                                </PopoverTrigger>
                                                                <PopoverContent className="w-[--radix-popover-trigger-width]">
                                                                    <Command>
                                                                        <CommandInput id="purchaseUomId" placeholder={t('product_form_purchase_uom_id_pop_over_place_holder')}/>
                                                                        <CommandList>
                                                                            <Command>{t('product_form_purchase_uom_id_pop_over_not_found')}</Command>
                                                                            <CommandGroup>
                                                                                {purchaseUoms.map((purchaseUom) => (
                                                                                    <CommandItem
                                                                                        key={purchaseUom.id}
                                                                                        value={purchaseUom.name}
                                                                                        onSelect={(currentValue) => {
                                                                                            setPurchaseUomPopOverLabel(currentValue === purchaseUomPopOverLabel ? "" : currentValue);
                                                                                            setOpenPurchaseUomPopOver(false);
                                                                                            form.setValue(
                                                                                                "purchaseUomId",
                                                                                                currentValue === purchaseUomPopOverLabel ? "" : purchaseUom.id,
                                                                                                {shouldTouch: true, shouldDirty: true, shouldValidate: currentValue !== purchaseUomPopOverLabel}
                                                                                            );
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
                                                        </FormControl>
                                                        <FormMessage className="text-xs text-destructive"/>
                                                    </FormItem>
                                                )}
                                            />
                                        </div>
                                    </div>
                                    <div className="lg:w-6/12 mb-4">
                                        <div className="flex flex-col space-y-1.5 mb-5">
                                            <FormField
                                                control={form.control}
                                                name="purchasePrice"
                                                render={({field}) => (
                                                    <FormItem>
                                                        <FormLabel>{t('product_form_purchase_price_label')}</FormLabel>
                                                        <FormControl>
                                                            <Input id="purchasePrice" type="number" {...field}
                                                                   disabled={mode === FormModeType.READ || isLoading}/>
                                                        </FormControl>
                                                        <FormMessage className="text-xs text-destructive"/>
                                                    </FormItem>
                                                )}
                                            />
                                        </div>
                                        <div className="flex flex-col space-y-1.5 mb-5">
                                            <FormField
                                                control={form.control}
                                                name="salePrice"
                                                render={({field}) => (
                                                    <FormItem>
                                                        <FormLabel>{t('product_form_sale_price_label')}</FormLabel>
                                                        <FormControl>
                                                            <Input id="salePrice" type="number" {...field}
                                                                   disabled={mode === FormModeType.READ || isLoading}/>
                                                        </FormControl>
                                                        <FormMessage className="text-xs text-destructive"/>
                                                    </FormItem>
                                                )}
                                            />
                                        </div>
                                    </div>
                                </div>
                                <div className="flex flex-row gap-4 mt-4 w-full">
                                    <div className="flex flex-row justify-start items-center space-x-1.5 w-4/12 gap-4">
                                        <FormField
                                            control={form.control}
                                            name="purchasable"
                                            render={({field}) => (
                                                <FormItem className="flex flex-row items-center space-x-2 space-y-0">
                                                    <FormControl>
                                                        <Checkbox id="purchasable" checked={field.value} disabled={mode === FormModeType.READ || isLoading}
                                                                  onCheckedChange={field.onChange}/>
                                                    </FormControl>
                                                    <FormLabel
                                                        className="font-normal">{t('product_form_purchasable_label')}</FormLabel>
                                                </FormItem>
                                            )}
                                        />
                                    </div>
                                    <div className="flex flex-row justify-start items-center space-x-1.5 w-4/12 gap-4">
                                        <FormField
                                            control={form.control}
                                            name="sellable"
                                            render={({field}) => (
                                                <FormItem className="flex flex-row items-center space-x-2 space-y-0">
                                                    <FormControl>
                                                        <Checkbox id="sellable" checked={field.value} disabled={mode === FormModeType.READ || isLoading}
                                                                  onCheckedChange={field.onChange}/>
                                                    </FormControl>
                                                    <FormLabel
                                                        className="font-normal">{t('product_form_sellable_label')}</FormLabel>
                                                </FormItem>
                                            )}
                                        />
                                    </div>
                                    <div className="flex flex-row justify-start items-center space-y-1.5 w-4/12 gap-4">
                                        <FormField
                                            control={form.control}
                                            name="active"
                                            render={({field}) => (
                                                <FormItem className="flex flex-row items-center space-x-2 space-y-0">
                                                    <FormControl>
                                                        <Checkbox id="active" checked={field.value} disabled={mode === FormModeType.READ || isLoading}
                                                                  onCheckedChange={field.onChange}/>
                                                    </FormControl>
                                                    <FormLabel
                                                        className="font-normal">{t('product_form_active_label')}</FormLabel>
                                                </FormItem>
                                            )}
                                        />
                                    </div>
                                </div>
                            </div>
                        </CardContent>
                        <CardFooter className="flex justify-between py-5">

                        </CardFooter>
                    </Card>
                </form>
            </Form>
        </div>
    );
};

export default ProductForm;
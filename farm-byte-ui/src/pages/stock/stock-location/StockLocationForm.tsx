import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import {useToast} from "@/hooks/use-toast.ts";
import {useSelector} from "react-redux";
import {RootState, store} from "@/Store.ts";
import {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
import {FormModeType} from "@/shared/model/FormModeType.ts";
import {z as Zod} from "zod";
import {unwrapResult} from "@reduxjs/toolkit";
import {zodResolver} from "@hookform/resolvers/zod";
import {
    createStockLocation,
    findStockLocationById,
    findStockLocations,
    getLoading,
    selectStockLocationById,
    selectStockLocations, updateStockLocation
} from "@/pages/stock/stock-location/StockLocationSlice.ts";
import {StockLocationModel} from "@/pages/stock/stock-location/StockLocationModel.ts";
import {DEFAULT_DIRECTION_VALUE, MAX_SIZE_VALUE} from "@/constants/page.constant.ts";
import {changeNullToEmptyString} from "@/utils/changeNullToEmptyString.ts";
import {StockLocationTypeEnum} from "@/pages/stock/stock-location/StockLocationTypeEnum.ts";
import {ToastType} from "@/shared/constant/globalConstant.ts";
import {cn} from "@/lib/utils.ts";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Toaster} from "@/components/ui/toaster.tsx";
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import FormCrudButton from "@/components/FormCrudButton.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Command, CommandGroup, CommandInput, CommandItem, CommandList} from "@/components/ui/command.tsx";
import {Checkbox} from "@/components/ui/checkbox.tsx";

const types = [
    {label: 'stock_location_type_label_internal', name: StockLocationTypeEnum.INTERNAL},
    {label: 'stock_location_type_label_customer', name: StockLocationTypeEnum.CUSTOMER},
    {label: 'stock_location_type_label_supplier', name: StockLocationTypeEnum.SUPPLIER},
    {label: 'stock_location_type_label_inventory', name: StockLocationTypeEnum.INVENTORY},
    {label: 'stock_location_type_label_view', name: StockLocationTypeEnum.VIEW},
    {label: 'stock_location_type_label_transit', name: StockLocationTypeEnum.TRANSIT},
]

const StockLocationForm = () => {

    const {t} = useTranslation(['home']);

    const {stockLocationId} = useParams();

    const {toast} = useToast();

    // @ts-ignore
    const stockLocation: StockLocationModel = useSelector((state:RootState) => selectStockLocationById(state, stockLocationId));
    const stockLocations: Array<StockLocationModel> = useSelector(selectStockLocations);
    const isLoading: boolean = useSelector(getLoading);

    const [openParentIdPopOver, setOpenParentIdPopOver] = useState(false);
    const [parentIdPopOverLabel, setParentIdPopOverLabel] = useState("");
    const [openTypePopOver, setOpenTypePopOver] = useState(false);
    const [typePopOverLabel, setTypePopOverLabel] = useState("");

    const navigate = useNavigate();
    const [mode, setMode] = useState<FormModeType>(stockLocationId? FormModeType.READ: FormModeType.CREATE);
    const StockLocationSchema = Zod.object({
        id: Zod.string().min(0),
        name: Zod.string().min(1, {message: t('stock_location_form_name_required_message')}),
        parentId: Zod.string().min(0),
        type: Zod.string().min(1, {message: t('stock_location_form_type_required_message')}),
        active: Zod.boolean()
    });
    const defaultValuesStockLocation: StockLocationModel = {
        id: "",
        name: "",
        parentId: "",
        type: "",
        active: true
    };
    const form = useForm<Zod.infer<typeof StockLocationSchema>>({
        defaultValues: defaultValuesStockLocation,
        resolver: zodResolver(StockLocationSchema),
        mode: "onChange"
    });

    useEffect(() => {
        store.dispatch(findStockLocations({page: 0, size: MAX_SIZE_VALUE, attribute: "name", direction: DEFAULT_DIRECTION_VALUE}));
    }, [store.dispatch]);

    useEffect(() => {
        if(!stockLocation && stockLocationId) {
            store.dispatch(findStockLocationById(stockLocationId));
        }
    }, [store.dispatch, stockLocation]);

    useEffect(() => {
        if(stockLocation) {
            // @ts-ignore
            form.reset(changeNullToEmptyString(stockLocation));
        }
    }, [stockLocation]);

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
        const stockLocationFormValue: StockLocationModel = form.getValues() as StockLocationModel;
        if (stockLocationFormValue.id) {
            store.dispatch(updateStockLocation({stockLocationId: stockLocationFormValue.id, stockLocation: stockLocationFormValue}))
                .then(unwrapResult)
                .then((response) => {
                    setMode(FormModeType.READ);
                    showToast("info", response.message);
                })
                .catch((error) => {
                    console.log(error);
                    setMode(FormModeType.EDIT);
                    showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                })
        } else {
            store.dispatch(createStockLocation(stockLocationFormValue))
                .then(unwrapResult)
                .then((response) => {
                    setMode(FormModeType.READ);
                    showToast("success", response.message);
                    navigate(`/stock/stock-locations/details/${response.content.id}`);
                })
                .catch((error) => {
                    setMode(FormModeType.CREATE);
                    showToast("danger", error !== null && error.reason !== null? t(error.reason) : t(error));
                });
        }
    }

    const onEdit = () => {
        setMode(FormModeType.EDIT);
    }

    const onCancel = () => {
        if(stockLocation) {
            form.reset(changeNullToEmptyString(stockLocation));
            setMode(FormModeType.READ);
            resetPopOverLabel(stockLocation);
        } else {
            form.reset();
            resetPopOverLabel(undefined);
        }
    }

    const onCreate = () => {
        navigate('/stock/stock-locations/new');
        setMode(FormModeType.CREATE);
        form.reset(defaultValuesStockLocation);
        resetPopOverLabel(undefined);
    }

    const resetPopOverLabel = (stockLocation: StockLocationModel | undefined) => {
        if(stockLocation) {
            setParentIdPopOverLabel(stockLocation.parentId? stockLocations.find(stockLocation => stockLocation.id === stockLocation.parentId)?.name as string: '');
            setTypePopOverLabel(types.find(type => stockLocation.type === type.name)?.label as string);
        } else {
            setTypePopOverLabel('');
            setParentIdPopOverLabel('');
        }
        setOpenTypePopOver(false);
        setOpenParentIdPopOver(false);
    }

    return (
        <div className="p-10">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} noValidate>
                    <Toaster/>
                    <Card className="">
                        <CardHeader>
                            <CardTitle className="flex flex-row justify-start items-center text-primary gap-5">
                        <span onClick={() => navigate(`/stock/stock-locations`)}
                              className="material-symbols-outlined text-3xl cursor-pointer">arrow_back</span>
                                <span
                                    className="text-2xl">{t(stockLocationId ? 'stock_location_form_edit_title' : 'stock_location_form_new_title')}</span>
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
                            <div className="grid md:grid-cols-2 w-full items-center gap-4">
                                <div className="flex flex-col space-y-1.5">
                                    <FormField
                                        control={form.control}
                                        name="name"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>{t('stock_location_form_name_label')}</FormLabel>
                                                <FormControl>
                                                    <Input id="name" type="text" {...field}
                                                           disabled={mode === FormModeType.READ || isLoading}/>
                                                </FormControl>
                                                <FormMessage className="text-xs text-destructive"/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <div className="flex flex-col space-y-1.5">
                                    <FormField
                                        control={form.control}
                                        name="parentId"
                                        render={() => (
                                            <FormItem>
                                                <FormLabel>{t('stock_location_form_parent_id_label')}</FormLabel>
                                                <Popover open={openParentIdPopOver}
                                                         onOpenChange={setOpenParentIdPopOver}>
                                                    <PopoverTrigger asChild>
                                                        <Button
                                                            variant="outline"
                                                            role="combobox"
                                                            aria-expanded={openParentIdPopOver}
                                                            className="w-full justify-between"
                                                            disabled={mode === FormModeType.READ || isLoading}
                                                        >
                                                        <span>{parentIdPopOverLabel
                                                            ? stockLocations.find((stockLocation) => stockLocation.name === parentIdPopOverLabel)?.name
                                                            : stockLocation ? stockLocations.find((sl) => stockLocation.parentId === sl.id)?.name : t('stock_location_parent_id_pop_over_place_holder')}</span>
                                                            <span
                                                                className="opacity-50 material-symbols-outlined">unfold_more</span>
                                                        </Button>
                                                    </PopoverTrigger>
                                                    <PopoverContent className="w-[--radix-popover-trigger-width]">
                                                        <Command>
                                                            <CommandInput id="parentId"
                                                                          placeholder="Search framework..."/>
                                                            <CommandList>
                                                                <Command>{t('stock_location_parent_id_pop_over_not_found')}</Command>
                                                                <CommandGroup>
                                                                    {stockLocations.map((stockLocation) => (
                                                                        <CommandItem
                                                                            key={stockLocation.id}
                                                                            value={stockLocation.name}
                                                                            onSelect={(currentValue) => {
                                                                                setParentIdPopOverLabel(currentValue === parentIdPopOverLabel ? "" : currentValue);
                                                                                setOpenParentIdPopOver(false);
                                                                                form.setValue("parentId",
                                                                                    currentValue === parentIdPopOverLabel ? "" : stockLocation.id,
                                                                                    {
                                                                                        shouldTouch: true,
                                                                                        shouldDirty: true,
                                                                                        shouldValidate: true
                                                                                    }
                                                                                );
                                                                            }}
                                                                        >
                                                                        <span
                                                                            className={`mr-2 h-4 w-4 material-symbols-outlined ${parentIdPopOverLabel === stockLocation.name ? 'opacity-100' : 'opacity-0'}`}
                                                                        >check</span>
                                                                            {stockLocation.name}
                                                                        </CommandItem>
                                                                    ))}
                                                                </CommandGroup>
                                                            </CommandList>
                                                        </Command>
                                                    </PopoverContent>
                                                </Popover>
                                                <FormMessage className="text-xs text-destructive"/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <div className="flex flex-col space-y-1.5">
                                    <FormField
                                        control={form.control}
                                        name="type"
                                        render={() => (
                                            <FormItem>
                                                <FormLabel>{t('stock_location_form_type_label')}</FormLabel>
                                                <FormControl>
                                                    <Popover open={openTypePopOver}
                                                             onOpenChange={setOpenTypePopOver}>
                                                        <PopoverTrigger asChild>
                                                            <Button
                                                                variant="outline"
                                                                role="combobox"
                                                                aria-expanded={openTypePopOver}
                                                                className="w-full justify-between"
                                                                disabled={mode === FormModeType.READ || isLoading}
                                                            >
                                                            <span>{typePopOverLabel
                                                                // @ts-ignore
                                                                ? t(types.find((type) => type.label === typePopOverLabel)?.label)
                                                                // @ts-ignore
                                                                : stockLocation ? t(types.find((typeEdit) => typeEdit.name === stockLocation.type)?.label) : t('stock_location_type_pop_over_place_holder')}</span>
                                                                <span
                                                                    className="opacity-50 material-symbols-outlined">unfold_more</span>
                                                            </Button>
                                                        </PopoverTrigger>
                                                        <PopoverContent className="w-[--radix-popover-trigger-width]">
                                                            <Command>
                                                                <CommandInput id="uomType"
                                                                              placeholder="Search framework..."/>
                                                                <CommandList>
                                                                    <Command>{t('stock_location_type_pop_over_not_found')}</Command>
                                                                    <CommandGroup>
                                                                        {types.map((type) => (
                                                                            <CommandItem
                                                                                key={type.label}
                                                                                value={type.label}
                                                                                onSelect={(currentValue) => {
                                                                                    setTypePopOverLabel(currentValue === typePopOverLabel ? "" : currentValue);
                                                                                    setOpenTypePopOver(false);
                                                                                    form.setValue("type",
                                                                                        currentValue === typePopOverLabel ? "" : type.name,
                                                                                        {
                                                                                            shouldTouch: true,
                                                                                            shouldDirty: true,
                                                                                            shouldValidate: true
                                                                                        }
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
                                <div className="flex flex-col space-y-1.5 mt-5">
                                    <FormField
                                        control={form.control}
                                        name="active"
                                        render={({field}) => (
                                            <FormItem className="flex flex-row items-center space-x-2 space-y-0">
                                                <FormControl>
                                                    <Checkbox id="active" checked={field.value}
                                                              disabled={mode === FormModeType.READ || isLoading}
                                                              onCheckedChange={field.onChange}/>
                                                </FormControl>
                                                <FormLabel
                                                    className="font-normal">{t('stock_location_form_active_label')}</FormLabel>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <div className="flex flex-col space-y-1.5">
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

export default StockLocationForm;
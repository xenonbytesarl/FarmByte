import {useTranslation} from "react-i18next";
import {useNavigate, useParams} from "react-router-dom";
import {useToast} from "@/hooks/use-toast.ts";
import {useSelector} from "react-redux";
import {RootState, store} from "@/Store.ts";
import {useEffect, useState} from "react";
import {FormModeType} from "@/shared/model/FormModeType.ts";
import {z as Zod} from "zod";
import {
    createUom,
    findUomById,
    getLoading,
    selectUomById,
    updateUom
} from "@/pages/stock/uom/UomSlice.ts";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {UomModel} from "@/pages/stock/uom/UomModel.ts";
import {UomTypeEnum} from "@/pages/stock/uom/UomTypeEnum.ts";
import {cn} from "@/lib/utils.ts";
import {unwrapResult} from "@reduxjs/toolkit";
import {Toaster} from "@/components/ui/toaster.tsx";
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import FormCrudButton from "@/components/FormCrudButton.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Command, CommandGroup, CommandInput, CommandItem, CommandList} from "@/components/ui/command.tsx";
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover.tsx";
import {Button} from "@/components/ui/button.tsx";
import {UomCategoryModel} from "@/pages/stock/uom-category/UomCategoryModel.ts";
import {findUomCategories, selectUomCategories} from "@/pages/stock/uom-category/UomCategorySlice.ts";
import {DEFAULT_DIRECTION_VALUE, MAX_SIZE_VALUE} from "@/constants/page.constant.ts";
import {ToastType} from "@/shared/constant/globalConstant.ts";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Checkbox} from "@/components/ui/checkbox.tsx";
import {changeNullToEmptyString} from "@/utils/changeNullToEmptyString.ts";

const uomTypes = [
    {label: 'uom_uom_type_label_reference', name: UomTypeEnum.REFERENCE},
    {label: 'uom_uom_type_label_lower', name: UomTypeEnum.LOWER},
    {label: 'uom_uom_type_label_greater', name: UomTypeEnum.GREATER},
]

const UomForm = () => {

    const {t} = useTranslation(['home']);

    const {uomId} = useParams();

    const {toast} = useToast();

    // @ts-ignore
    const uom: UomModel = useSelector((state:RootState) => selectUomById(state, uomId));
    const uomCategories: Array<UomCategoryModel> = useSelector(selectUomCategories);
    const isLoading: boolean = useSelector(getLoading);

    const [openUomCategoryPopOver, setOpenUomCategoryPopOver] = useState(false);
    const [uomCategoryPopOverLabel, setUomCategoryPopOverLabel] = useState("");
    const [openUomTypePopOver, setOpenUomTypePopOver] = useState(false);
    const [uomTypePopOverLabel, setUomTypePopOverLabel] = useState("");

    const navigate = useNavigate();
    const [mode, setMode] = useState<FormModeType>(uomId? FormModeType.READ: FormModeType.CREATE);
    const UomSchema = Zod.object({
        id: Zod.string().min(0),
        name: Zod.string().min(1, {message: t('uom_form_name_required_message')
        }),
        uomCategoryId: Zod.string().min(1, {message: t('uom_form_uom_category_id_required_message')}),
        uomType: Zod.string().min(1, {message: t('uom_form_uom_type_required_message')}),
        ratio: Zod.coerce.number().positive({message: t('uom_form_ratio_positive_message')}).or(Zod.string().min(0)),
        active: Zod.boolean()
    });
    const defaultValuesUom: UomModel = {
        id: "",
        name: "",
        uomCategoryId: "",
        uomType: UomTypeEnum.REFERENCE,
        ratio: 1.0,
        active: true
    };
    const form = useForm<Zod.infer<typeof UomSchema>>({
        defaultValues: defaultValuesUom,
        resolver: zodResolver(UomSchema),
        mode: "onChange"
    });

    useEffect(() => {
        store.dispatch(findUomCategories({page: 0, size: MAX_SIZE_VALUE, attribute: "name", direction: DEFAULT_DIRECTION_VALUE}));
    }, [store.dispatch]);

    useEffect(() => {
        if(!uom && uomId) {
            store.dispatch(findUomById(uomId));
        }
    }, [store.dispatch, uom]);

    useEffect(() => {
        if(uom) {
            // @ts-ignore
            form.reset(changeNullToEmptyString(uom));
        }
    }, [uom]);

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
        const uomFormValue: UomModel = form.getValues() as UomModel;
        if (uomFormValue.id) {
            store.dispatch(updateUom({uomId: uomFormValue.id, uom: uomFormValue}))
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
            store.dispatch(createUom(uomFormValue))
                .then(unwrapResult)
                .then((response) => {
                    setMode(FormModeType.READ);
                    showToast("success", response.message);
                    navigate(`/stock/uoms/details/${response.content.id}`);
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
        if(uom) {
            form.reset(changeNullToEmptyString(uom));
            setMode(FormModeType.READ);
            resetPopOverLabel(uom);
        } else {
            form.reset();
            resetPopOverLabel(undefined);
        }
    }

    const onCreate = () => {
        navigate('/stock/uoms/new');
        setMode(FormModeType.CREATE);
        form.reset(defaultValuesUom);
       resetPopOverLabel(undefined);
    }

    const resetPopOverLabel = (uom: UomModel | undefined) => {
        if(uom) {
            setUomCategoryPopOverLabel(uomCategories.find(uomCategory => uomCategory.id === uom.uomCategoryId)?.name as string);
            setUomTypePopOverLabel(uomTypes.find(type => uom.uomType === type.name)?.label as string);
        } else {
            setUomCategoryPopOverLabel('');
            setUomTypePopOverLabel('');
        }
        setOpenUomCategoryPopOver(false);
        setOpenUomTypePopOver(false);
    }

    return (
        <div className="p-10">
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} noValidate>
                    <Toaster/>
                    <Card className="">
                        <CardHeader>
                            <CardTitle className="flex flex-row justify-start items-center text-primary gap-5">
                        <span onClick={() => navigate(`/stock/uoms`)}
                              className="material-symbols-outlined text-3xl cursor-pointer">arrow_back</span>
                                <span
                                    className="text-2xl">{t(uomId ? 'uom_form_edit_title' : 'uom_form_new_title')}</span>
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
                                                <FormLabel>{t('uom_category_form_name_label')}</FormLabel>
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
                                        name="uomCategoryId"
                                        render={() => (
                                            <FormItem>
                                                <FormLabel>{t('uom_form_uom_category_id_label')}</FormLabel>
                                                <Popover open={openUomCategoryPopOver}
                                                         onOpenChange={setOpenUomCategoryPopOver}>
                                                    <PopoverTrigger asChild>
                                                        <Button
                                                            variant="outline"
                                                            role="combobox"
                                                            aria-expanded={openUomCategoryPopOver}
                                                            className="w-full justify-between"
                                                            disabled={mode === FormModeType.READ || isLoading}
                                                        >
                                                        <span>{uomCategoryPopOverLabel
                                                            ? uomCategories.find((uomCategory) => uomCategory.name === uomCategoryPopOverLabel)?.name
                                                            : uom ? uomCategories.find((uomCategory) => uom.uomCategoryId === uomCategory.id)?.name : t('uom_category_pop_over_place_holder')}</span>
                                                            <span
                                                                className="opacity-50 material-symbols-outlined">unfold_more</span>
                                                        </Button>
                                                    </PopoverTrigger>
                                                    <PopoverContent className="w-[--radix-popover-trigger-width]">
                                                        <Command>
                                                            <CommandInput id="uomCategoryId"
                                                                          placeholder="Search framework..."/>
                                                            <CommandList>
                                                                <Command>{t('uom_category_pop_over_not_found')}</Command>
                                                                <CommandGroup>
                                                                    {uomCategories.map((uomCategory) => (
                                                                        <CommandItem
                                                                            key={uomCategory.id}
                                                                            value={uomCategory.name}
                                                                            onSelect={(currentValue) => {
                                                                                setUomCategoryPopOverLabel(currentValue === uomCategoryPopOverLabel ? "" : currentValue);
                                                                                setOpenUomCategoryPopOver(false);
                                                                                form.setValue("uomCategoryId",
                                                                                    currentValue === uomCategoryPopOverLabel ? "" : uomCategory.id,
                                                                                    {shouldTouch: true, shouldDirty: true, shouldValidate: true}
                                                                                );
                                                                            }}
                                                                        >
                                                                        <span
                                                                            className={`mr-2 h-4 w-4 material-symbols-outlined ${uomCategoryPopOverLabel === uomCategory.name ? 'opacity-100' : 'opacity-0'}`}
                                                                        >check</span>
                                                                            {uomCategory.name}
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
                                        name="ratio"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>{t('uom_form_ratio_label')}</FormLabel>
                                                <FormControl>
                                                    <Input id="ratio" type="number" {...field}
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
                                        name="uomType"
                                        render={() => (
                                            <FormItem>
                                                <FormLabel>{t('uom_form_uom_type_label')}</FormLabel>
                                                <FormControl>
                                                    <Popover open={openUomTypePopOver} onOpenChange={setOpenUomTypePopOver}>
                                                        <PopoverTrigger asChild>
                                                            <Button
                                                                variant="outline"
                                                                role="combobox"
                                                                aria-expanded={openUomTypePopOver}
                                                                className="w-full justify-between"
                                                                disabled={mode === FormModeType.READ || isLoading}
                                                            >
                                                            <span>{uomTypePopOverLabel
                                                                // @ts-ignore
                                                                ? t(uomTypes.find((uomType) => uomType.label === uomTypePopOverLabel)?.label)
                                                                // @ts-ignore
                                                                : uom ? t(uomTypes.find((uomTypeEdit) => uomTypeEdit.name === uom.uomType)?.label) : t('uom_pop_over_place_holder')}</span>
                                                                <span
                                                                    className="opacity-50 material-symbols-outlined">unfold_more</span>
                                                            </Button>
                                                        </PopoverTrigger>
                                                        <PopoverContent className="w-[--radix-popover-trigger-width]">
                                                            <Command>
                                                                <CommandInput id="uomType"
                                                                              placeholder="Search framework..." />
                                                                <CommandList>
                                                                    <Command>{t('uom_pop_over_not_found')}</Command>
                                                                    <CommandGroup>
                                                                        {uomTypes.map((uomType) => (
                                                                            <CommandItem
                                                                                key={uomType.label}
                                                                                value={uomType.label}
                                                                                onSelect={(currentValue) => {
                                                                                    setUomTypePopOverLabel(currentValue === uomTypePopOverLabel ? "" : currentValue)
                                                                                    setOpenUomTypePopOver(false);
                                                                                    form.setValue("uomType",
                                                                                        currentValue === uomTypePopOverLabel ? "": uomType.name,
                                                                                        {shouldTouch: true, shouldDirty: true, shouldValidate: true}
                                                                                    );
                                                                                }}
                                                                            >
                                                                            <span
                                                                                className={`mr-2 h-4 w-4 material-symbols-outlined ${uomTypePopOverLabel === uomType.label ? 'opacity-100' : 'opacity-0'}`}
                                                                            >check</span>
                                                                                {t(uomType.label)}
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
                                                    <Checkbox id="active" checked={field.value} disabled={mode === FormModeType.READ || isLoading}
                                                              onCheckedChange={field.onChange}/>
                                                </FormControl>
                                                <FormLabel
                                                    className="font-normal">{t('uom_category_form_active_label')}</FormLabel>
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

export default UomForm;
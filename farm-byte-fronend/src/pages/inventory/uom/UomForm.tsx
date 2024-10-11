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
} from "@/pages/inventory/uom/UomSlice.ts";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {UomModel} from "@/pages/inventory/uom/UomModel.ts";
import {UomTypeEnum} from "@/pages/inventory/uom/UomTypeEnum.ts";
import {cn} from "@/lib/utils.ts";
import {unwrapResult} from "@reduxjs/toolkit";
import {Toaster} from "@/components/ui/toaster.tsx";
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import FormCrudButton from "@/components/FormCrudButton.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Label} from "@/components/ui/label.tsx";
import {Switch} from "@/components/ui/switch.tsx";
import {Command, CommandGroup, CommandInput, CommandItem, CommandList} from "@/components/ui/command.tsx";
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover.tsx";
import {Button} from "@/components/ui/button.tsx";
import {UomCategoryModel} from "@/pages/inventory/uom-category/UomCategoryModel.ts";
import {findUomCategories, selectUomCategories} from "@/pages/inventory/uom-category/UomCategorySlice.ts";
import {DEFAULT_DIRECTION_VALUE, MAX_SIZE_VALUE} from "@/constants/page.constant.ts";
import {ToastType} from "@/shared/constant/globalConstant.ts";

const uomTypes = [
    {label: 'uom_uom_type_label_reference', name: UomTypeEnum.REFERENCE},
    {label: 'uom_uom_type_label_lower', name: UomTypeEnum.LOWER},
    {label: 'uom_uom_type_label_greater', name: UomTypeEnum.GREATER},
]

const UomForm = () => {

    const {t} = useTranslation(['home']);

    const {uomId} = useParams();

    const {toast} = useToast();

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
        name: Zod.string().min(1, t('uom_form_name_required_message')),
        uomCategoryId: Zod.string().min(1, t('uom_form_uom_category_id_required_message')),
        uomType: Zod.string().min(1, t('uom_form_uom_type_required_message')),
    });
    const defaultValuesUom: UomModel = {
        id: "",
        name: "",
        uomCategoryId: "",
        uomType: UomTypeEnum.REFERENCE,
        ratio: 1.0,
        active: true
    };
    const { register, handleSubmit, formState: {errors, isValid}, reset, getValues, setValue } = useForm<UomModel>({
        defaultValues: defaultValuesUom,
        resolver: zodResolver(UomSchema),
        mode: "onTouched"
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
            reset(uom)
        }
    }, [uom, reset]);

    useEffect(() => {
        if(uomCategoryPopOverLabel) {
            setValue(
                'uomCategoryId',
                uomCategories.find(uomCategory => uomCategory.name === uomCategoryPopOverLabel).id,
                {shouldTouch: true, shouldDirty: true, shouldValidate: true});
        }
    }, [uomCategoryPopOverLabel]);

    useEffect(() => {
        if(uomTypePopOverLabel) {
            setValue(
                'uomType',
                uomTypes.find(uomType => uomType.label === uomTypePopOverLabel).name,
                {shouldTouch: true, shouldDirty: true, shouldValidate: true});
        }
    }, [uomTypePopOverLabel]);

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
        const uomFormValue: UomModel = getValues();
        if (uomFormValue.id) {
            store.dispatch(updateUom({uomId: uomFormValue.id, uom: uomFormValue}))
                .then(unwrapResult)
                .then((response) => {
                    setMode(FormModeType.READ);
                    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                    // @ts-expect-error
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
                    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                    // @ts-expect-error
                    showToast("success", response.message);
                    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                    // @ts-expect-error
                    navigate(`/inventory/uoms/details/${response.data.content.id}`);
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
            reset(uom);
            setMode(FormModeType.READ);
        } else {
            reset();
        }
    }

    const onCreate = () => {
        navigate('/inventory/uoms/new');
        setMode(FormModeType.CREATE);
        reset(defaultValuesUom);
        setUomCategoryPopOverLabel('');
        setUomTypePopOverLabel('');
        setOpenUomCategoryPopOver(false);
        setOpenUomTypePopOver(false);
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)} noValidate>
            <Toaster/>
            <Card className="">
                <CardHeader>
                    <CardTitle className="flex flex-row justify-start items-center text-primary gap-5">
                        <span onClick={() => navigate(`/inventory/uoms`)}
                              className="material-symbols-outlined text-3xl cursor-pointer">arrow_back</span>
                        <span
                            className="text-2xl">{t(uomId ? 'uom_form_edit_title' : 'uom_form_new_title')}</span>
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
                    <div className="grid md:grid-cols-2 w-full items-center gap-4">
                        <Input id="name" type="hidden" {...register("id")} />

                        <div className="flex flex-col space-y-1.5">
                            <Label htmlFor="name">{t('uom_category_form_name_label')}</Label>
                            <Input id="name" type="text" {...register("name")}
                                   disabled={mode === FormModeType.READ || isLoading}/>
                            <small className="text-red-500">{errors.name?.message}</small>
                        </div>
                        <div className="flex flex-col space-y-1.5">
                            <Label htmlFor="uomCategoryId">{t('uom_form_uom_category_id_label')}</Label>
                            <Popover open={openUomCategoryPopOver} onOpenChange={setOpenUomCategoryPopOver}>
                                <PopoverTrigger asChild>
                                    <Button
                                        variant="outline"
                                        role="combobox"
                                        aria-expanded={openUomCategoryPopOver}
                                        className="justify-between"
                                        disabled={mode === FormModeType.READ || isLoading}
                                    >
                                        <span>{uomCategoryPopOverLabel
                                            ? uomCategories.find((uomCategory) => uomCategory.name === uomCategoryPopOverLabel)?.name
                                            : uom? uomCategories.find((uomCategory) => uom.uomCategoryId === uomCategory.id)?.name: t('uom_category_pop_over_place_holder')}</span>
                                        <span className="opacity-50 material-symbols-outlined" >unfold_more</span>
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-[--radix-popover-trigger-width]">
                                    <Command>
                                        <CommandInput  id="uomCategoryId" placeholder="Search framework..." {...register("uomCategoryId")}/>
                                        <CommandList>
                                            <Command>{t('uom_category_pop_over_not_found')}</Command>
                                            <CommandGroup>
                                                {uomCategories.map((uomCategory) => (
                                                    <CommandItem
                                                        key={uomCategory.id}
                                                        value={uomCategory.name}
                                                        onSelect={(currentValue) => {
                                                            console.log(currentValue)
                                                            setUomCategoryPopOverLabel(currentValue === uomCategoryPopOverLabel ? "" : currentValue)
                                                            setOpenUomCategoryPopOver(false)
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
                            <small className="text-red-500">{errors.uomCategoryId?.message}</small>
                        </div>
                        <div className="flex flex-col space-y-1.5">
                            <Label htmlFor="ratio">{t('uom_form_ratio_label')}</Label>
                            <Input id="ratio" type="number" {...register("ratio")}
                                   disabled={mode === FormModeType.READ || isLoading}/>
                            <small className="text-red-500">{errors.ratio?.message}</small>
                        </div>
                        <div className="flex flex-col space-y-1.5">
                            <Label htmlFor="uomType">{t('uom_form_uom_type_label')}</Label>
                            <Popover open={openUomTypePopOver} onOpenChange={setOpenUomTypePopOver}>
                                <PopoverTrigger asChild>
                                    <Button
                                        variant="outline"
                                        role="combobox"
                                        aria-expanded={openUomTypePopOver}
                                        className="justify-between"
                                        disabled={mode === FormModeType.READ || isLoading}
                                    >
                                        <span>{uomTypePopOverLabel
                                            ? t(uomTypes.find((uomType) => uomType.label === uomTypePopOverLabel)?.label)
                                            : uom? t(uomTypes.find((uomTypeEdit) => uomTypeEdit.name === uom.uomType)?.label): t('uom_pop_over_place_holder')}</span>
                                        <span className="opacity-50 material-symbols-outlined" >unfold_more</span>
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-[--radix-popover-trigger-width]">
                                    <Command>
                                        <CommandInput  id="uomType" placeholder="Search framework..." {...register("uomType")}/>
                                        <CommandList>
                                            <Command>{t('uom_pop_over_not_found')}</Command>
                                            <CommandGroup>
                                                {uomTypes.map((uomType) => (
                                                    <CommandItem
                                                        key={uomType.label}
                                                        value={uomType.label}
                                                        onSelect={(currentValue) => {
                                                            setUomTypePopOverLabel(currentValue === uomTypePopOverLabel ? "" : currentValue)
                                                            setOpenUomTypePopOver(false)
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
                            <small className="text-red-500">{errors.uomType?.message}</small>
                        </div>
                        <div className="flex flex-col space-y-1.5">
                            <Switch id="active" {...register("active")}/>
                            <Label htmlFor="active">{t('uom_form_active_label')}</Label>
                        </div>

                        <div className="flex flex-col space-y-1.5">

                        </div>
                    </div>
                </CardContent>
                <CardFooter className="flex justify-between py-5">

                </CardFooter>
            </Card>
        </form>
    );
};

export default UomForm;